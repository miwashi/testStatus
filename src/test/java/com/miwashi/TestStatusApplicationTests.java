package com.miwashi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import net.miwashi.teststatus.TestStatusApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestStatusApplication.class)
@WebAppConfiguration
public class TestStatusApplicationTests {

	private String udpHost = "localhost";
	private int udpPort = 41234;
	
	@Test
	public void shouldSendUdpPackage() {
		sendByUDP("Hello World");
	}

	protected void sendByUDP(String msg) {
        try {
            DatagramSocket sock = new DatagramSocket();
            InetAddress addr = InetAddress.getByName(udpHost);
            byte[] message = (msg + "\n").getBytes();
            DatagramPacket packet = new DatagramPacket(message, message.length, addr, udpPort);
            sock.send(packet);
            sock.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
