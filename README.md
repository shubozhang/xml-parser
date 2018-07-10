This project introduces XML parser in Java. 

JAXP (Java API for XML Processing) provides three basic parsing interfaces: 
```
*DOM: the Document Object Model parsing interface
*SAX: the Simple API for XML parsing interface
*StAX: the Streaming API for XML or StAX interface (part of JDK 6; separate jar available for JDK 5)
```

JAXB (Java Architecture for XML Binding) allows Java developers to map Java classes to XML representations, using JAXB annotation to convert Java object to / from XML file.

```
JDOM
Woodstox
XOM
dom4j
VTD-XML
Xerces-J
Crimson
```


1 - How do I make JAXB go faster?
You are on the right track with unmarshalling from a StAX input, but I would recommend a XMLStreamReader instead of a XMLEventReader.
```java
XMLInputFactory xmlif = XMLInputFactory.newInstance();
XMLStreamReader xmler = xmlif.createXMLStreamReader(fr);
Since StAX is a standard you can switch in another implementation such as WoodStox as the underlying parser.
```

2 - How can I be 100% sure what underlying parsing mechanism it is using?
Just like you are doing. If you pass a JAXB implementation an instance of XMLStreamReader then you can be reasonably sure that it is being used. 
If on the other hand you unmarshalled from something like an InputStream then the JAXB implementation is free to use whatever parsing technique it wants to. 
If you go with Woodstox be sure to check out there performance page as well:
