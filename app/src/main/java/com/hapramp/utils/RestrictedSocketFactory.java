package com.hapramp.utils;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

public class RestrictedSocketFactory extends SocketFactory {
  private static final String TAG = RestrictedSocketFactory.class.getSimpleName();

  private int mSendBufferSize;


  public RestrictedSocketFactory(int sendBufferSize) {
    mSendBufferSize = sendBufferSize;
    try {
      Socket socket = new Socket();
      Log.w(TAG, String.format("Changing SO_SNDBUF on new sockets from %d to %d.",
        socket.getSendBufferSize(), sendBufferSize));
    }
    catch (SocketException e) {
      //
    }
  }

  @Override
  public Socket createSocket()
    throws IOException {
    return updateSendBufferSize(new Socket());
  }

  @Override
  public Socket createSocket(String host, int port)
    throws IOException, UnknownHostException {
    return updateSendBufferSize(new Socket(host, port));
  }

  @Override
  public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
    throws IOException, UnknownHostException {
    return updateSendBufferSize(new Socket(host, port, localHost, localPort));
  }

  @Override
  public Socket createSocket(InetAddress host, int port)
    throws IOException {
    return updateSendBufferSize(new Socket(host, port));
  }

  @Override
  public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
    throws IOException {
    return updateSendBufferSize(new Socket(address, port, localAddress, localPort));
  }

  private Socket updateSendBufferSize(Socket socket)
    throws IOException {
    socket.setSendBufferSize(mSendBufferSize);
    return socket;
  }
}
