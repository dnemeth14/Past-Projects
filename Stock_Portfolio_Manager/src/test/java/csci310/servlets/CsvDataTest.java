package csci310.servlets;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import csci310.utilities.ParseCsv;

@PrepareForTest({ParseCsv.class})
@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
public class CsvDataTest extends Mockito {
	
    @Test
	public void testDoPost() throws ServletException, IOException {
    	// test case 1
		HttpServletRequest requestPass1 = mock(HttpServletRequest.class);
        HttpServletResponse responsePass1 = mock(HttpServletResponse.class);
         
		StringWriter stringWriter = new StringWriter();
	    PrintWriter writer = new PrintWriter(stringWriter);
	    when(responsePass1.getWriter()).thenReturn(writer);
	    when(requestPass1.getParameter("index")).thenReturn("AAPL");
	   
		CsvData csvdata = new CsvData();
		csvdata.doPost(requestPass1,responsePass1);
		
		writer.flush();
		
		String expectedPassResponse = "[{1598227200=[10.0]}, {1598659200=[100.0]}, {1578614400=[1000.0]}]";
        Assert.assertEquals(expectedPassResponse, stringWriter.toString());
        
        // test case 2
        HttpServletRequest requestPass2 = mock(HttpServletRequest.class);
        HttpServletResponse responsePass2 = mock(HttpServletResponse.class);
        
		StringWriter stringWriter2 = new StringWriter();
	    PrintWriter writer2 = new PrintWriter(stringWriter2);
	    when(responsePass2.getWriter()).thenReturn(writer2);
	    when(requestPass2.getParameter("index")).thenReturn("GOOGL");
	   
		csvdata.doPost(requestPass2,responsePass2);
		
		writer2.flush();
		
		String expectedPassResponse_GOOGL = "[{1578614400=[10.0]}, {1578614400=[100.0]}, {1578614500=[1000.0]}]";
	    Assert.assertEquals(expectedPassResponse_GOOGL, stringWriter2.toString());
	}

}
