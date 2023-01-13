/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boutique;

/**
 *
 * @author IsraelPerezMasle
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionMySQL {
    private static Connection myConn;
    private static PreparedStatement myStmt;
    private static ResultSet myRs;
    private String Url = "jdbc:mysql://localhost:3306/boutique?useTimezone=true&serverTimezone=UTC";
    private String User = "root";//usuario
    private String Pass = "";//clave
    private static String registro = "";
    
    public ConexionMySQL(){}
    
    public void Conectar(){//consultando base de datos
        String registros="";
        String select = "SELECT * FROM "
                + "cliente LEFT JOIN RecoleccionDePedidos ON idCliente = idR_pedidos "
                + "LEFT JOIN articulo ON idArticulo = idR_pedidos LEFT JOIN ArticulosDePedidos ON idA_pedidos = idR_pedidos";
        try{
            myConn = DriverManager.getConnection(Url, User, Pass);
            myStmt = myConn.prepareStatement(select);
            myRs = myStmt.executeQuery();
            while(myRs.next()){
                registros +=
                    myRs.getString("idCliente")
                  + "|" + myRs.getString("empresa") + "|" + myRs.getString("fecha") + "|" + myRs.getString("numeroPedido") 
                  + "|" + myRs.getString("numeroCliente") + "|" + myRs.getString("nombre") + "|" + myRs.getString("direcion")
                  + "|" + myRs.getString("telefono") + "|" + myRs.getString("cantidadSurtida") + "|" + myRs.getString("cantidadPedida")
                  + "|" + myRs.getString("seccionBodega") + "|" + myRs.getString("numeroEstante") + "|" + myRs.getString("nombreArticulo")
                  + "|" + myRs.getString("descripcion") + "|" + myRs.getString("tamano") + "|" + myRs.getString("color")
                  + "|" + myRs.getString("precio")
                  + "^";
            }
            registro = registros;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void insert(String nombreEmpresa, String nombreCliente, String nombreProducto, String direccion, String telefono, String seccion, String numeroEstante, String descripcion, String cantidadSurtida, String cantidadOrdenada, String tamano, String color, String precio){
        String numArticulo = String.valueOf(Math.round(Math.random()*37048));
        try{
            myStmt = myConn.prepareStatement("Insert into Cliente(numeroCliente,nombre,direcion,telefono,empresa) values(?,?,?,?,?)");
            myStmt.setDouble(1, Math.round(Math.random()*42587));
            myStmt.setString(2, nombreCliente);
            myStmt.setString(3, direccion);
            myStmt.setString(4, telefono);
            myStmt.setString(5, nombreEmpresa);
            myStmt.executeUpdate();
            
            myStmt = myConn.prepareStatement("Insert into Articulo(numeroArticulo,precio,cantidadExistente) values(?,?,?)");
            myStmt.setInt(1, Integer.valueOf(numArticulo));
            myStmt.setDouble(2, Double.valueOf(precio));
            myStmt.setInt(3, Integer.valueOf(cantidadOrdenada));
            myStmt.executeUpdate();
            
            myStmt = myConn.prepareStatement("Insert into recolecciondepedidos(numeroPedido,nombreArticulo,cantidadArticulos) values(?,?,?)");
            myStmt.setDouble(1, Math.round(Math.random()*104857));
            myStmt.setString(2, nombreProducto);
            myStmt.setInt(3, Integer.valueOf(cantidadSurtida));
            myStmt.executeUpdate();
            
            myStmt = myConn.prepareStatement("Insert into ArticulosDePedidos(numeroArticulo,descripcion,tamano,color,seccionBodega,cantidadPedida,cantidadSurtida,numeroEstante) values(?,?,?,?,?,?,?,?)");
            myStmt.setInt(1, Integer.valueOf(numArticulo));
            myStmt.setString(2, descripcion);
            myStmt.setString(3, tamano);
            myStmt.setString(4, color);
            myStmt.setString(5, seccion);
            myStmt.setInt(6, Integer.valueOf(cantidadOrdenada));
            myStmt.setInt(7, Integer.valueOf(cantidadSurtida));
            myStmt.setInt(8, Integer.valueOf(numeroEstante));
            myStmt.executeUpdate();
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void delete(int id){
        try{
            String delete[] = new String[4];
            delete[0] = "DELETE FROM cliente WHERE idCliente=?";
            delete[1] = "DELETE FROM articulo WHERE idArticulo=?";
            delete[2] = "DELETE FROM RecoleccionDePedidos WHERE idR_pedidos=?";
            delete[3] = "DELETE FROM ArticulosDePedidos WHERE idA_pedidos=?";
            myStmt = myConn.prepareStatement(delete[3]);
            myStmt.setInt(1, id);
            myStmt.executeUpdate();
            myStmt = myConn.prepareStatement(delete[2]);
            myStmt.setInt(1, id);
            myStmt.executeUpdate();
            myStmt = myConn.prepareStatement(delete[1]);
            myStmt.setInt(1, id);
            myStmt.executeUpdate();
            myStmt = myConn.prepareStatement(delete[0]);
            myStmt.setInt(1, id);
            myStmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void update(String tabla, String campo, String campo1, int id, String dato){
        String Update = "UPDATE " + tabla + " SET " + campo + "=? WHERE " + campo1 + "=?";
        try{
            myStmt = myConn.prepareStatement(Update);
            if(campo.equals("cantidadExistente") || campo.equals("cantidadArticulos") || campo.equals("cantidadPedida") || campo.equals("cantidadSurtida")){
                myStmt.setInt(1, Integer.valueOf(dato));
            }else{
                myStmt.setString(1, dato);
            }
            myStmt.setInt(2, id);
            myStmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public String getRegistro(){
        return registro;
    }
}
