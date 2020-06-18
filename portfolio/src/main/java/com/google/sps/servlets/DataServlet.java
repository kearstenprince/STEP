// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList;

import com.google.appengine.api.datastore.*;
import java.io.*;
import java.util.*;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List <String> comments = new ArrayList();

  @Override
  public void init() throws ServletException {
        comments.add("Hello");
        comments.add("My");
        comments.add("name");
        comments.add("is");
        comments.add("Kearsten");
        comments.add("Kearsten");
    }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json;");
    response.getWriter().println(comments);
  }
  private String commentsToJson(List<Comment> comments){
      Gson gson = new Gson();
      return gson.toJson(comments);
  }
  private List<Comment> getcomments(){
      Query query = new Query("Comment").addSort("time", SortDirection.DESCENDING);
      List <Comment> comments = new ArrayList();
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);

      /* for(Entity entity: results.asIterable())
      {
          String text = (String) entity.getProperty("text");
          long time = (long) entity.getProperty("time");
          comments.add(new Comment(text,time));
      } */
      return comments;
      }
  /*private List <String> getCommentsMock(){
        List <String> comments = new ArrayList();
        comments.add("Hello");
        comments.add("My");
        comments.add("name");
        comments.add("is");
        comments.add("Kearsten");
        return comments;
      }*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String text = request.getParameter("comment_post_ID");
    if(text == null || text.equals("")){
        return;
    }

    addComment(text, System.currentTimeMillis());
    response.sendRedirect("/index.html");
  }
    private void addComment(String commentText, long time){
        Entity commentEntity = new Entity("comment");
        commentEntity.setProperty("text", commentText);
        commentEntity.setProperty("time", time);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
    }

    private class Comment{
        String text;
        long time;

        Comment(String text, long time){
            this.text = text;
            this.time = time;
        }
    }
}

