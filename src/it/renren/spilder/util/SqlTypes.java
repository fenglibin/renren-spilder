package it.renren.spilder.util;

import java.sql.Types;
import java.sql.PreparedStatement;
import java.io.InputStream;
import java.sql.Clob;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;

/**
 *
 * <p>Title: 数据库类型匹配</p>
 *
 * <p>Description: 数据库类型匹配</p>
 *
 * <p>Copyright: Copyright (c) 2004 Sunline.</p>
 *
 * <p>Company: Sunline Technologies.</p>
 *
 * @author qiumh
 * @version 1.0
 */
public class SqlTypes
{
    /**
     * 定义类型转换器
     */
    private static Hashtable _typeConvertors = new Hashtable();

    /**
     * 根据类型取得相应转换器
     * @param type String 转换器类型
     * @return SQLTypeConvertor
     */
    public static SQLTypeConvertor getConvertor(String type)
    {
        Object obj = _typeConvertors.get(type);
        if(null != obj)
        {
            return (SQLTypeConvertor)obj;
        }
        else
        {
            return new SQLTypeConvertor()
            {
                public Object convert(String obj, String param)
                {
                    return obj;
                }
            };
        }
    }

    /**
     * 设置PS参数值
     * @param stmt PreparedStatement
     * @param index int
     * @param value Object
     * @param sqlType int
     * @throws SQLException
     */
    public static void setObject( PreparedStatement stmt, int index, Object value, int sqlType )
            throws SQLException
    {
        if (value == null) {
            stmt.setNull( index, sqlType );
        } else {
            // Special processing for BLOB and CLOB types, because they are mapped by Castor to
            // java.io.InputStream and java.io.Reader, respectively,
            // while JDBC driver expects java.sql.Blob and java.sql.Clob.
            switch ( sqlType ) {
            case Types.FLOAT:
            case Types.DOUBLE:
                stmt.setDouble( index, ((Double)value).doubleValue() );
                break;
            case Types.REAL:
                stmt.setFloat( index, ((Float)value).floatValue() );
                break;
            case Types.BLOB:
                try {
                    InputStream stream = (InputStream) value;
                    stmt.setBinaryStream(index, stream, stream.available());
                } catch (IOException ex) {
                    throw new SQLException(ex.toString());
                }
                break;
            case Types.CLOB:
                Clob clob = (Clob) value;
                stmt.setCharacterStream(index, clob.getCharacterStream(),
                        (int) Math.min(clob.length(), Integer.MAX_VALUE));
                break;
            default:
                stmt.setObject(index, value, sqlType);
                break;
            }
        }
    }

    /**
     * 根据字段类型取字段值
     * @param rs ResultSet
     * @param index int
     * @param sqlType int
     * @return Object
     * @throws SQLException
     */
    public static Object getObject( ResultSet rs, int index, int sqlType )
            throws SQLException
    {
        Object value;
        long longVal;
        int intVal;
        boolean boolVal;
        double doubleVal;
        float floatVal;
        short shortVal;
        byte byteVal;

        switch ( sqlType ) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return rs.getString(index);
        case Types.DECIMAL:
        case Types.NUMERIC:
            return rs.getBigDecimal( index );
        case Types.INTEGER:
            intVal = rs.getInt( index );
            return ( rs.wasNull() ? null : new Integer( intVal ) );
        case Types.TIME:
            return rs.getTime( index );
        case Types.DATE:
            return rs.getDate( index );
        case Types.TIMESTAMP:
            return rs.getTimestamp( index );
        case Types.FLOAT:
        case Types.DOUBLE:
            doubleVal = rs.getDouble( index );
            return ( rs.wasNull() ? null : new Double( doubleVal ) );
        case Types.REAL:
            floatVal = rs.getFloat( index );
            return ( rs.wasNull() ? null : new Float( floatVal ) );
        case Types.SMALLINT:
            shortVal = rs.getShort( index );
            return ( rs.wasNull() ? null : new Short( shortVal ) );
        case Types.TINYINT:
            byteVal = rs.getByte( index );
            return ( rs.wasNull() ? null : new Byte( byteVal ) );
        case Types.LONGVARBINARY:
        case Types.VARBINARY:
        case Types.BINARY:
            return rs.getBytes(index);
        case Types.BLOB:
            Blob blob = rs.getBlob( index );
            return (blob == null ? null :  blob.getBinaryStream());
        case Types.CLOB:
            return rs.getClob( index );
        case Types.BIGINT:
            longVal = rs.getLong( index );
            return ( rs.wasNull() ? null : new Long( longVal ) );
        case Types.BIT:
            boolVal = rs.getBoolean( index );
            return ( rs.wasNull() ? null : new Boolean( boolVal ) );
        default:
            value = rs.getObject( index );
            return ( rs.wasNull()? null : value );
        }
    }

    /**
     * Returns the SQL type from the specified Java type. Returns <tt>OTHER</tt>
     * if the Java type has no suitable SQL type mapping. The argument
     * <tt>sqlType</tt> must be the return value from a previous call to
     * {@link #typeFromSQLType} or {@link #typeFromName}.
     *
     * @param javaType The Java class of the SQL type
     * @return SQL type from the specified Java type
     */
    public static int getSQLType( Class javaType )
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
        	/*判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口。*/
            if ( _typeInfos[ i ].javaType.isAssignableFrom( javaType ) )
                return _typeInfos[ i ].sqlType;
        }
        return java.sql.Types.OTHER;
    }
    
    /*用JAVA Bean存储*/
    static class TypeInfo
    {
        final int    sqlType;

        final String sqlTypeName;

        final Class  javaType;

        TypeInfo( int sqlType, String sqlTypeName, Class javaType )
        {
            this.sqlType     = sqlType;
            this.sqlTypeName = sqlTypeName;
            this.javaType  = javaType;
        }
    }
  
    public static class NullType
    {
        private Class javaType;
        public NullType(Class javaType)
        {
            this.javaType = javaType;
        }
        public Class getClassType()
        {
            return javaType;
        }
    }
    
    public abstract static class SQLTypeConvertor
    {
        public abstract Object convert( String obj, String param );
    }

    static
    {
        _typeConvertors.put("String",new SQLTypeConvertor(){
            public Object convert(String obj,String param)
            {
                return obj;
            }
        });
        _typeConvertors.put("long",new SQLTypeConvertor(){
            public Object convert(String obj,String param)
            {
                if(null == obj || "".equals(obj))
                    return null;
                return Long.valueOf(obj);
            }
        });
        _typeConvertors.put("double",new SQLTypeConvertor(){
            public Object convert(String obj,String param)
            {
                if(null == obj || "".equals(obj))
                    return null;
                return Double.valueOf(obj);
            }
        });
        //goutf@sunline.cn 2005-11-06添加，解决基本交易中对money类型的数据支持
        _typeConvertors.put("money",new SQLTypeConvertor(){
            public Object convert(String obj,String param)
            {
                if(null == obj || "".equals(obj))
                    return null;
                return Double.valueOf(obj);
            }
        });

        _typeConvertors.put("Timestamp",new SQLTypeConvertor(){
            public Object convert(String obj, String param)
            {
                if(null == obj || "".equals(obj))
                {
                    return null;
                }
                try
                {
                    String pattern = null;
                    if(null == param || "".equals(param))
                    {
                        if( -1 == obj.indexOf(" "))
                        {
                            pattern = "yyyy-MM-dd";
                        }
                        else
                        {
                            pattern = "yyyy-MM-dd HH:mm:ss";
                        }
                    }
                    else
                    {
                        pattern = param;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                    return new Timestamp(sdf.parse(obj).getTime());
                }
                catch(ParseException except)
                {
                    throw new IllegalArgumentException(except.toString());
                }
            }
        });
        _typeConvertors.put("file",new SQLTypeConvertor(){
            public Object convert(String obj,String param)
            {
                if(null == obj || "".equals(obj))
                    return new ByteArrayInputStream(new byte[0]);
                try
                {
                    BASE64Decoder base64de = new BASE64Decoder();
                    return new ByteArrayInputStream(base64de.decodeBuffer(obj));
                }
                catch(Exception e)
                {
                    throw new IllegalArgumentException(e.toString());
                }
            }
        });
    }

    /**
     * List of all the SQL types supported.
     */
    static TypeInfo[] _typeInfos = new TypeInfo[] {
        new TypeInfo( java.sql.Types.BIT,           "bit",           java.lang.Boolean.class ),
        new TypeInfo( java.sql.Types.TINYINT,       "tinyint",       java.lang.Byte.class ),
        new TypeInfo( java.sql.Types.SMALLINT,      "smallint",      java.lang.Short.class ),
        new TypeInfo( java.sql.Types.INTEGER,       "integer",       java.lang.Integer.class ),
        new TypeInfo( java.sql.Types.BIGINT,        "bigint",       java.math.BigInteger.class ),
        new TypeInfo( java.sql.Types.BIGINT,        "bigint",        java.lang.Long.class ),
        new TypeInfo( java.sql.Types.FLOAT,         "float",         java.lang.Double.class ),
        new TypeInfo( java.sql.Types.DOUBLE,        "double",        java.lang.Double.class ),
        new TypeInfo( java.sql.Types.REAL,          "real",          java.lang.Float.class ),
        new TypeInfo( java.sql.Types.NUMERIC,       "numeric",       java.math.BigDecimal.class ),
        new TypeInfo( java.sql.Types.DECIMAL,       "decimal",       java.math.BigDecimal.class ),
        new TypeInfo( java.sql.Types.VARCHAR,       "varchar",       java.lang.String.class ),
        new TypeInfo( java.sql.Types.CHAR,          "char",          java.lang.String.class ),
        new TypeInfo( java.sql.Types.LONGVARCHAR,   "longvarchar",   java.lang.String.class ),
        new TypeInfo( java.sql.Types.DATE,          "date",          java.sql.Date.class ),
        new TypeInfo( java.sql.Types.TIME,          "time",          java.sql.Time.class ),
        new TypeInfo( java.sql.Types.TIMESTAMP,     "timestamp",     java.sql.Timestamp.class ),
        new TypeInfo( java.sql.Types.BINARY,        "binary",        byte[].class ),
        new TypeInfo( java.sql.Types.VARBINARY,     "varbinary",     byte[].class ),
        new TypeInfo( java.sql.Types.LONGVARBINARY, "longvarbinary", byte[].class ),
        new TypeInfo( java.sql.Types.BLOB,          "blob",          java.io.InputStream.class ),
        new TypeInfo( java.sql.Types.CLOB,          "clob",          java.sql.Clob.class ),
        new TypeInfo( java.sql.Types.OTHER,         "other",         java.lang.Object.class ),
        new TypeInfo( java.sql.Types.JAVA_OBJECT,   "javaobject",    java.lang.Object.class )
    };
}
