package com.volvo.gcc3.interiorroom.request.response;

import java.io.StringReader;


class XMLReaderWithoutNamespace extends StringReader {
    
    public XMLReaderWithoutNamespace(String reader) {
      super(reader);
    }
    public String getAttributeNamespace(int arg0) {
      return "";
    }
    public String getNamespaceURI() {
      return "";
    }
}

