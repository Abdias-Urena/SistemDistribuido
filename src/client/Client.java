package client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JFileChooser;

/**
 *
 * @author Abdias
 */
public class Client {
    static Socket sfd = null;
    public static void main(String[] args) throws IOException {
        sfd = new Socket("172.20.10.4", 8000);
        DataInputStream inputStream = new DataInputStream(
                new BufferedInputStream(
                    sfd.getInputStream()));
        DataOutputStream outputStream = new DataOutputStream(
                new BufferedOutputStream(
                    sfd.getOutputStream()));
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(null);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            
            // obtengo el path
            File archivo = fileChooser.getSelectedFile();
            String path = archivo.getAbsolutePath();
            //int totalServidores = 3;
            System.out.println(path);
            
            // leer el archivo en bytes
            File file = new File(path);
            byte[] fileBytes = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(fileBytes, 0, fileBytes.length);
            
            
            
        }else{
            System.out.println("No selecciono nada");
        }
    }
    
    
}
