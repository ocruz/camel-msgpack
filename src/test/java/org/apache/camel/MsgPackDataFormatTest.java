package org.apache.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MsgPackDataFormatTest extends CamelTestSupport {

	@Test
	public void testMarshalAndUnmarshallerMap() throws Exception {
		TestMessage testMessage = new TestMessage();
		testMessage.setTest("msgPack");
		MockEndpoint mock = getMockEndpoint("mock:reverse");
		mock.expectedMessageCount(1);
		mock.message(0).body().isInstanceOf(TestMessage.class);
		mock.message(0).body().isEqualTo(testMessage);

		Object marshaled = template.requestBody("direct:in", testMessage);
		template.sendBody("direct:back", marshaled);
		mock.assertIsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				MsgPackDataFormat format = MsgPackDataFormat.of(TestMessage.class);
				from("direct:in").marshal(format);
				from("direct:back").unmarshal(format).to("mock:reverse");
			}
		};
	}
}
