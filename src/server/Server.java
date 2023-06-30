/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Abdias
 */
public class Server{

    public static void main(String[] args) throws IOException {
        int puerto = 1234;  // Puerto en el que el servidor escucha las conexiones

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            for (int i = 0; i < 3; i++) {
                Socket socket = serverSocket.accept();
                recibirParte(socket, i);
                socket.close();
            }
        }

        // El último servidor recopila las partes y envía el archivo completo de vuelta al cliente
        unirYEnviarArchivo();
    }

    private static void recibirParte(Socket socket, int parteIndex) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;

        InputStream is = socket.getInputStream();
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream("ruta/de/almacenamiento/parte_" + parteIndex + ".txt"));

        while ((bytesRead = is.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();
    }

    private static void unirYEnviarArchivo() throws IOException {
        int totalPartes = 4;
        int parteSize = 1024;
        byte[] archivoCompleto = new byte[totalPartes * parteSize];

        // Leer las partes del archivo
        for (int i = 0; i < totalPartes; i++) {
            byte[] parte = leerParteDelArchivo("ruta/de/almacenamiento/parte_" + i + ".txt");
            System.arraycopy(parte, 0, archivoCompleto, i * parteSize, parte.length);
        }

        // Enviar el archivo completo al cliente
        String clienteIP = "192.168.0.2";  // Dirección IP del cliente
        int clientePuerto = 5678;  // Puerto del cliente

        try (Socket socket = new Socket(clienteIP, clientePuerto)) {
            OutputStream os = socket.getOutputStream();
            os.write(archivoCompleto);
            os.flush();
        }
    }

    private static byte[] leerParteDelArchivo(String rutaParte) throws IOException {
        File file = new File(rutaParte);
        byte[] parteBytes = new byte[(int) file.length()];

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(parteBytes, 0, parteBytes.length);
        bis.close();

        return parteBytes;
    }

}
