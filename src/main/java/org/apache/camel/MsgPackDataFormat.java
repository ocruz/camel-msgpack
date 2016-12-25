package org.apache.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatName;
import org.apache.camel.support.ServiceSupport;
import org.apache.camel.util.ExchangeHelper;
import org.apache.camel.util.ObjectHelper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A <a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})
 * for Msgpack data.
 */
public class MsgPackDataFormat extends ServiceSupport implements DataFormat, DataFormatName {

	private static final String DATA_FORMAT_NAME = "msgPack";

	private ObjectMapper objectMapper;
	private final Class<?> templateClass;

	private MsgPackDataFormat(Class<?> template) {
		this.templateClass = template;

		MessagePackFactory messagePackFactory = new MessagePackFactory();
		this.objectMapper = new ObjectMapper(messagePackFactory);

	}

	public String getDataFormatName() {
		return DATA_FORMAT_NAME;
	}

	public static MsgPackDataFormat of(Class<?> template){
		return new MsgPackDataFormat(template);
	}

	public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
		// marshal from the Java object (graph) to the msgPack type
		if(this.templateClass == null) {
			throw new Exception("Template class is null");
		}

		Object template = ExchangeHelper.convertToMandatoryType(exchange, templateClass, graph);
		ObjectHelper.notNull(template, "Body can't be null for marshalling");
		stream.write(objectMapper.writeValueAsBytes(template));
	}

	public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
		// unmarshal from the input stream of msgPack to Java object (graph)
		if(this.templateClass == null) {
			throw new Exception("Template class is null");
		}

		byte[] bytes = exchange.getContext().getTypeConverter().mandatoryConvertTo(byte[].class, stream);
		return objectMapper.readValue(bytes, templateClass);
	}

	@Override
	protected void doStart() throws Exception {
		if(templateClass == null){
			throw new IllegalStateException("Template class wasn't provided");
		}
	}

	@Override
	protected void doStop() throws Exception {
	}
}
