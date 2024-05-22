package com.example.Topic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class PublisherOffers {
	final static String EXCHANGE_NAME = "eskaintzak";
	final static int NUM_VALORES = 100;
	ConnectionFactory factory;
	
	public PublisherOffers() {
		factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
		//puerto 5672 o 5673 para TLS
	}
	public void enviarMensaje() throws InterruptedException {
		try(Connection connection = factory.newConnection()){
			
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");			
			
			
			for (int i = 0; i<NUM_VALORES; i++) {
				VentaProductos venta = new VentaProductos();
				String topic = venta.getJarduera()+"."+venta.getKategoria()+"."+venta.getEnpresa();			
				channel.basicPublish(EXCHANGE_NAME,topic, MessageProperties.PERSISTENT_TEXT_PLAIN, venta.toString().getBytes());
				
				System.out.println("Eskaintza argitaratuta: " + venta.toString());
				Thread.sleep(1000);
			}
			channel.close ();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws InterruptedException {
		PublisherOffers publisher = new PublisherOffers();
		publisher.enviarMensaje();
        Thread hiloEspera = new Thread(() -> {
        });
        hiloEspera.start();
	}
}