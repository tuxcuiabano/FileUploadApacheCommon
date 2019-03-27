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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author tuxcuiabano
 */
@WebServlet(urlPatterns = {"/Upload"})
public class Upload extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    
       private void inserirImagemDiretorio(FileItem item) throws IOException, ClassNotFoundException, SQLException {


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

 

 
//Guarda no banco de dados o endereço para recuperação da imagem
Connection conexao = null;


/**

  Estabeleça a conexão

*/

          String driver = "org.apache.derby.jdbc.ClientDriver";
          String url = "jdbc:derby://localhost:1527/webmail";
          String username = "root";
          String password = "root123";
          Class.forName(driver);
          conexao = DriverManager.getConnection(url, username, password);

          PreparedStatement declaracao = null;
          String sql = "INSERT INTO TabelaTeste2 (codigo, imagem) VALUES(?, ?) ";

 

try {

            declaracao = conexao.prepareStatement(sql);

            declaracao.setInt(1, 3); // codigo 1

            declaracao.setString(2, caminho+nome);

            declaracao.executeUpdate();

          
            
            
            
            
             } catch (SQLException sqlEx) {

                        sqlEx.printStackTrace();

            } catch (Exception ex) {

                  ex.printStackTrace();

            }

    }

    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          
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

                        if (!item.isFormField()) {

                            if (item.getName().length() > 0) {

                                this.inserirImagemDiretorio(item);
                                
                out.println("<div align='center'><img src='imagens/jiflogo.png' width='349' height='254'><br></div>");
                out.println("<div align='center'>Usuário incluido com sucesso<br/></div>");
                out.println("<div align='center'><a href='menulogado.jsp'>Voltar</a></div>"); 

                            }

                        }

                    }

                }

catch (FileUploadException ex) {

   ex.printStackTrace();

                }

catch (Exception ex) {

   ex.printStackTrace();

                }

            }

    }
            
                
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
