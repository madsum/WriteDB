// ******************************************************************************
// * Copyright (c) 2009 Ford Motor Company. All Rights Reserved.
// * Original author: Ford Motor Company J2EE Center of Excellence
// *
// * $RCSfile: AbstractQuery.java,v $
// * $Revision$
// * $Author$
// * $Date$
// *
// ******************************************************************************
package com.volvo.gcc3.interiorroom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract class that implements common query-methods like close and rollback.
 */
public abstract class AbstractQuery {

    /**
     * The value that we store a boolean true as in the database.
     */
    public final static String DATABASE_TRUE = "Y";

    /**
     * The value that we store a boolean false as in the database.
     */
    public final static String DATABASE_FALSE = "N";

    /**
     * Closes the given statement if it is not null, and handles any
     * SQLException that occurs by ignoring it.
     * 
     * @param statement
     */
    public static void close(CallableStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // Do nothing
            }
        }
    }

    /**
     * Closes the given statement if it is not null, and handles any
     * SQLException that occurs by ignoring it.
     * 
     * @param statement
     */
    public static void close(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                // Do nothing
            }
        }
    }

    /**
     * Closes the given statement if it is not null, and handles any
     * SQLException that occurs by ignoring it.
     * 
     * @param statement
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                // Do nothing
            }
        }
    }

    /**
     * Closes the given resultset if it is not null, and handles any
     * SQLException that occurs by ignoring it.
     * 
     * @param rs
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                // Do nothing
            }
        }
    }

    /**
     * Closes the given connection if it is not null, and handles any
     * SQLException that occurs by ignoring it.
     * 
     * @param connection
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                // Do nothing
            }
        }
    }

    /**
     * Rolls back the previous statements, if the connection variable is not
     * null, and handles any SQLException that occures by ignoring it.
     * 
     * @param connection
     */
    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                // Do nothing
            }
        }
    }

    /**
     * Closes the given writer if it is not null, and handles any IOException
     * that occurs by ignoring it.
     * 
     * @param writer
     */
    public static void close(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    /**
     * Closes the given inputStream if it is not null, and handles any
     * IOException that occurs by ignoring it.
     * 
     * @param inputStream
     */
    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    /**
     * Closes the given reader if it is not null, and handles any IOException
     * that occurs by ignoring it.
     * 
     * @param reader
     */
    public static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    /**
     * Creates a map that can be used by query-classes loading items in order.
     * 
     * @param <K>
     * @param <V>
     * @return Map<K, V>
     */
    public static <K, V> Map<K, V> createMap() {
        return new LinkedHashMap<K, V>();
    }

    /**
     * Commits all transactions for the given connection object. If it fails, a
     * GCCResourceException will be thrown.
     * 
     * @param connection
     * @throws GCCResourceException
     */
    public static void commit(Connection connection) {
        if (connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                System.out.println("Ex: " + e.getMessage());
            }
        } else {
            System.out.println("What to do now ");
        }
    }
}
