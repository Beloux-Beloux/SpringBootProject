package com.medical.ui.utils;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private final CloseableHttpClient http = HttpClients.createDefault();
    private final ObjectMapper mapper;
    public ApiClient() { mapper = new ObjectMapper(); mapper.registerModule(new JavaTimeModule()); mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); }
    public <T> T get(String path, Class<T> type) throws Exception { return execute(new HttpGet(url(path)), type); }
    public <T> T post(String path, Object body, Class<T> type) throws Exception { HttpPost r=new HttpPost(url(path)); r.setEntity(new org.apache.hc.core5.http.io.entity.StringEntity(mapper.writeValueAsString(body), ContentType.APPLICATION_JSON)); return execute(r,type); }
    public <T> T put(String path, Object body, Class<T> type) throws Exception { HttpPut r=new HttpPut(url(path)); r.setEntity(new org.apache.hc.core5.http.io.entity.StringEntity(mapper.writeValueAsString(body), ContentType.APPLICATION_JSON)); return execute(r,type); }
    public void delete(String path) throws Exception { try(CloseableHttpResponse r=http.execute(new HttpDelete(url(path)))) { int s=r.getCode(); if(s<200||s>=300) throw new ApiException(s,responseMessage(r)); } }
    public String encode(String value){return URLEncoder.encode(value, StandardCharsets.UTF_8);}
    private <T> T execute(ClassicHttpRequest request, Class<T> type)throws Exception{try(CloseableHttpResponse r=http.execute(request)){int s=r.getCode();String body=r.getEntity()==null?"":new String(r.getEntity().getContent().readAllBytes(),StandardCharsets.UTF_8);if(s<200||s>=300)throw new ApiException(s,errorMessage(body));if(type==Void.class||body.isBlank())return null;return mapper.readValue(body,type);}}
    private String url(String path){return Config.API_BASE_URL+(path.startsWith("/")?path:"/"+path);}
    private String responseMessage(CloseableHttpResponse r){try{if(r.getEntity()==null)return "Erreur HTTP "+r.getCode();return new String(r.getEntity().getContent().readAllBytes(),StandardCharsets.UTF_8);}catch(IOException e){return "Erreur HTTP "+r.getCode();}}
    private String errorMessage(String body){try{JsonNode n=mapper.readTree(body);if(n.has("message"))return n.get("message").asText();}catch(Exception ignored){}return body.isBlank()?"Erreur API":body;}
}
