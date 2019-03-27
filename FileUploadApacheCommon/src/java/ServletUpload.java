/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;




/**
 *
 * @author tuxcuiabano
 */
@WebServlet(urlPatterns = {"/ServletUpload"})
public class ServletUpload extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * 
     */
    




  //Process the HTTP Post request

    public void doPost(HttpServletRequest request, HttpServletResponse response)

                       throws ServletException, IOException {

        doGet(request, response);

    }
    
    
    
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

 

            boolean isMultiPart = FileUpload.isMultipartContent(request);

            if (isMultiPart) {

                FileItemFactory factory = new DiskFileItemFactory();

                ServletFileUpload upload = new ServletFileUpload(factory);

                String formulario = "";

                try {

                    List items = upload.parseRequest(request);

                    Iterator iter = items.iterator();

                    while (iter.hasNext()) {

                        FileItem item = (FileItem) iter.next();

                        if (item.getFieldName().equals("tipoForm")) {

                            formulario = item.getString();

                        }

                        
                        
                        
                        if (item.isFormField()) {

                           PrintWriter out = response.getWriter();
                            String idd = item.getString();        	   
                            out.println("retorno incerto: " + idd);  
                            request.setAttribute("message",
                            " foi feito upload com sucesso! "+idd);

                            }
                            else{
                                 if (item.getName().length() > 0) {

                                this.inserirImagemDiretorio(item, 33);
                                request.setAttribute("message",
                            " foi feito upload com sucesso! ");
                            
                            
                            
                            
                            }
                            
                            

                        }

                    }

                }

catch (FileUploadException ex) {

   ex.printStackTrace();
    request.setAttribute("message",
                    "There was an error: " + ex.getMessage());

                }

catch (Exception ex) {

   ex.printStackTrace();

                }

            }
             getServletContext().getRequestDispatcher("/mensagem.jsp").forward(
                request, response);
        

    }

  

    
      /**

     *

     * @param item FileItem, representa um arquivo que é enviado pelo formulario

     * MultiPart/Form-data

     * @throws IOException

     */
   
    

    private void inserirImagemDiretorio(FileItem item, int codigo) throws IOException, ClassNotFoundException, SQLException {


            //Pega o diretório /logo dentro do diretório atual de onde a
            //aplicação está rodando

            String caminho = getServletContext().getRealPath("/logo") + "/";

             // Cria o diretório caso ele não exista

            File diretorio = new File(caminho);

            if (!diretorio.exists()){
                diretorio.mkdir();
            }


            // Mandar o arquivo para o diretório informado

            String nome = item.getName();
            String arq[] = nome.split("\\\\");

            for (int i = 0; i < arq.length; i++) {

                nome = arq[i];

            }

 

            File file = new File(diretorio, nome);

            FileOutputStream output = new FileOutputStream(file);

            InputStream is = item.getInputStream();

            byte[] buffer = new byte[2048];

            int nLidos;

            while ((nLidos = is.read(buffer)) >= 0) {

                output.write(buffer, 0, nLidos);

            }

            output.flush();

            output.close();

/**

  Estabeleça a conexão

*/

          String driver = "org.apache.derby.jdbc.ClientDriver";
          String url = "jdbc:derby://localhost:1527/webmail";
          String username = "root";
          String password = "root123";
          Class.forName(driver);
          Connection conexao = DriverManager.getConnection(url, username, password);

          PreparedStatement declaracao = null;
          String sql = "INSERT INTO TabelaTeste2 (codigo, imagem) VALUES(?, ?) ";
        
 

try {

            declaracao = conexao.prepareStatement(sql);

            declaracao.setInt(1, codigo); // codigo 1

            declaracao.setString(2, caminho+nome);

            declaracao.executeUpdate();
            
             } catch (SQLException sqlEx) {

                        sqlEx.printStackTrace();

            } catch (Exception ex) {

                  ex.printStackTrace();

            }

    }



}

