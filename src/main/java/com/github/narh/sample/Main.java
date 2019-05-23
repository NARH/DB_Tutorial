/*
 * Copyright (c) 2018, NARH https://github.com/NARH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.narh.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author narita
 *
 */
public class Main {

  static Connection conn;

  public static void main(String ...args) throws Exception {
    conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
    migration();
    Map<String,Integer> columns = getColumnFromTable(new String[] {"test","test_dtl"});

    String sql = "select test.*, test_dtl.* from test left outer join test_dtl on test_dtl.id = test.id";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    ResultSetMetaData rsmd = rs.getMetaData();
    for(int i = 0; i < rsmd.getColumnCount(); i++)
      System.out.printf("%s.%s\n",rsmd.getTableName(i+1), rsmd.getColumnLabel(i+1));

    while(rs.next()) {
      System.out.printf("%s=%s\n", "TEST.ID", rs.getString(columns.get("TEST.ID")));
      System.out.printf("%s=%s\n", "TEST_DTL.ID", rs.getString(columns.get("TEST_DTL.ID")));
      System.out.printf("%s=%s\n", "TEST_DTL.SEQ", rs.getString(columns.get("TEST_DTL.SEQ")));
      System.out.printf("%s=%s\n", "TEST.REG_DATE", rs.getString(columns.get("TEST.REG_DATE")));
      System.out.printf("%s=%s\n", "TEST_DTL.REG_DATE", rs.getString(columns.get("TEST_DTL.REG_DATE")));
    }
    rs.close();
    stmt.close();
    conn.close();
  }

  private static void migration() throws SQLException {
    execute("create table test (id int primary key,name varchar,reg_date datetime, update_date datetime)");
    execute("insert into test values (1, 'hoge',now(), now())");
    execute("create table test_dtl (id int,seq int,reg_date datetime, update_date datetime, primary key(id,seq))");
    execute("insert into test_dtl values (1, 1, now(), now())");
    execute("insert into test_dtl values (1, 2, now(), now())");
  }

  private static void execute(final String sql) throws SQLException {
    System.out.println("sql:"+sql);
    Statement stmt = conn.createStatement();
    stmt.execute(sql);
    stmt.close();
  }

  private static Map<String, Integer> getColumnFromTable(final String ...tables) throws SQLException {
    Map<String, Integer> columns = new HashMap<>();
    int index = 0;
    for(String table:tables) {
      List<String> results = getColumnFromTable(table);
      for(String column:results)
        columns.put(column,++index);
    }
    return columns;
  }

  private static List<String> getColumnFromTable(final String table) throws SQLException {
    String sql = "select * from " + table + " where ROWNUM <1";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    ResultSetMetaData rsmd = rs.getMetaData();
    List<String> columns = new ArrayList<String>();
    for(int i = 0; i < rsmd.getColumnCount(); i++) {
      columns.add(
          (rsmd.getTableName(i+1).isEmpty()) ? table.toUpperCase() : rsmd.getTableName(i+1).toUpperCase()
          + "."
          + rsmd.getColumnName(i+1).toUpperCase());
    }
    rs.close();
    stmt.close();
    return columns;
  }
}
