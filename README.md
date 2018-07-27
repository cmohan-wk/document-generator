# document-generator
====================

The Companies house Document Generator for generating documents for accounts

# Requirements
--------------

In order to build document-generator locally you will need the following:
- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

# Getting started
-----------------

1. Run make
2. Run ./start.sh


Environment Variables
---------------------
to be added

document-generator-interface
===================================
The document-generator-interface is a sub module within document-generator, it holds the interface
DocumentInforService that contains the methods to getDocumentInfo in order to return the DocumentInfo model
which will contain String data, String assetId, String templateId