package com.example.Topic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class SuscriberOffers {
	final static String EXCHANGE_NAME = "eskaintzak";
	final static String QUEUE_NAME = "eskaintza_ilara";
	ConnectionFactory factory;

	public SuscriberOffers() {
		factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
	}

	public void suscribe(Integer jardueraNum, Integer kategoriaNum, String enpresa) {

		Channel channel = null;
		String[] jarduerak = { "Lana", "Kurtsoa" };
		String[] kategoriak = { "IT", "Marketing", "Finantzak", "Osasuna", "Heziketa" };
		String jarduera = jarduerak[jardueraNum - 1];
		String kategoria = kategoriak[kategoriaNum - 1];

		try (Connection connection = factory.newConnection()) {

			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");

			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
			String topic = jarduera + "." + kategoria;
			if (enpresa.length() == 0)
				topic = topic + ".*";
			else
				topic = topic + "." + enpresa;
			System.out.println(topic);
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, topic);

			MiConsumer consumer = new MiConsumer(channel);
			boolean autoack = true;
			String tag = channel.basicConsume(QUEUE_NAME, autoack, consumer);

			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			channel.basicCancel(tag);
			channel.close();

		} catch (IOException | TimeoutException e) {

			e.printStackTrace();
		}

	}

	public synchronized void stop() {
		this.notify();
	}

	public class MiConsumer extends DefaultConsumer {

		public MiConsumer(Channel channel) {
			super(channel);

		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
				throws IOException {
			String message = new String(body, "UTF-8");
			System.out.println("Mensaje recibido:  " + message);
		}

	}

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		SuscriberOffers suscriber = new SuscriberOffers();
		System.out.println("Lana edo Kurtsoa?");
		System.out.println("1- Lana.");
		System.out.println("2- Kurtsoa.");
		//Integer jarduera = teclado.nextInt();teclado.nextLine();
		System.out.println("Escribe el sector al que te quieres susbscribir.");
		System.out.println("1- IT.");
		System.out.println("2- Marketing.");
		System.out.println("3- Finantzak.");
		System.out.println("4- Osasuna.");
		System.out.println("5- Heziketa.");
		//Integer kategoria = teclado.nextInt();teclado.nextLine();
		System.out.println("Ze enpresara nahi duzu izena eman?(Hutsik utzi ez nahi baduzu enpresarik)");
		String enpresa = teclado.nextLine();
		System.out.println(" Esperando mensaje. Pulsa return para terminar");
		Thread hiloEspera = new Thread(() -> {
			teclado.nextLine();
			suscriber.stop();
		});
		hiloEspera.start();
		//suscriber.suscribe(jarduera, kategoria, enpresa);
		suscriber.suscribe(1, 1, enpresa);
		teclado.close();
	}
}
