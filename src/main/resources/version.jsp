<%@ page import ="java.net.*"%>
<%@ page import ="com.ibm.websphere.runtime.ServerName"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>@project@ Version</TITLE>
</HEAD>
<BODY>
<H1>@project@</H1>

<table border="1">
<tr><td>Version</td><td>@version@</td></tr>
<tr><td>Build Timestamp</td><td>@buildTimestamp@</td></tr>
<tr><td>Branch</td><td>@branch@</td></tr>
<tr><td>Build Tag Name</td><td>@buildTag@</td></tr>
<% try { %>
<tr><td>CmaLogEnv</td><td><%=System.getProperty("CmaLogEnv") %></td></tr>
<tr><td>CmaLogDir</td><td><%=System.getProperty("CmaLogDir") %></td></tr>
<% } catch (Exception e) {} %>
</table>

<H2>System Information</H2>
<table border="1">
<%
  try {
    out.println("<tr><td>Host Name</td><td>" + InetAddress.getLocalHost().getHostName() + "</td></tr>");
  } catch (Exception e) {
  	out.println("<tr><td>Error displaying host name</td><td>" + e.getMessage() + "</td></tr>");
  }

  try {
    out.println("<tr><td>Server Process</td><td>" + ServerName.getDisplayName() + "</td></tr>");
    out.println("<tr><td>Server Full Name</td><td>" + ServerName.getFullName() + "</td></tr>");
  } catch (Exception e) {
  	out.println("<tr><td>Error displaying ServerName</td><td>" + e.getMessage() + "</td></tr>");
  }

  try {
    Runtime rt = Runtime.getRuntime();
    long freebytes = rt.freeMemory();
    long totalbytes = rt.totalMemory();
    long usedbytes = totalbytes - freebytes;
    rt = null;

    long free = freebytes/(1024 * 1024);
    long total = totalbytes/(1024 * 1024);
    long used = total - free;

    out.println("<tr><td>Total Memory</td><td>" + total + " MB   (" + totalbytes + " Bytes) </td></tr>");
    out.println("<tr><td>Free Memory</td><td>" + free + " MB   (" + freebytes + " Bytes) </td></tr>");
    out.println("<tr><td>Used Memory</td><td>" + used + " MB   (" + usedbytes + " Bytes) </td></tr>");

  } catch (Exception e) {
  	out.println("<tr><td>Error displaying memory information</td><td>" + e.getMessage() + "</td></tr>");
  }
%>
</table>
</BODY>
</HTML>
