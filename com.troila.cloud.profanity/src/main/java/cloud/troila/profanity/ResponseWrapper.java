package cloud.troila.profanity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {


    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private PrintWriter printWriter;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new MyServletOutputStream(bytes); 
    }
  
    @Override
    public PrintWriter getWriter() throws IOException {
        try{
        	printWriter = new PrintWriter(new OutputStreamWriter(bytes, "utf-8"));
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return printWriter;
    }

    public byte[] getBytes() {
        if(printWriter != null) {
        	printWriter.close();
        } 

        if(bytes != null) {
            try {
                bytes.flush();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return bytes.toByteArray();
    }

    class MyServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream os ;

        public MyServletOutputStream(ByteArrayOutputStream ostream) {
            this.os = ostream;
        }

        @Override
        public void write(int b) throws IOException {
            os.write(b);
        }

    }
	
}
