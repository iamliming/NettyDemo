package jibx;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import domain.Address;
import domain.Customer;
import domain.Order;
import domain.Shipping;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 27, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TestOrder
{
    private IBindingFactory factory;
    private StringWriter writer;
    private StringReader reader;
    private static final String CHARSET = "UTF-8";

    private String encode2Xml(Order order)
        throws JiBXException, IOException
    {
        factory = BindingDirectory.getFactory(Order.class);
        writer = new StringWriter();
        IMarshallingContext marshallingContext = factory.createMarshallingContext();
        marshallingContext.setIndent(2);
        marshallingContext.marshalDocument(order, CHARSET, null, writer);
        String xmlStr = writer.toString();
        writer.close();
        System.out.println(xmlStr);
        return xmlStr;
    }

    private Order decode2Order(String xmlBody)
        throws JiBXException
    {
        reader = new StringReader(xmlBody);
        IUnmarshallingContext unmarshallingContext = factory.createUnmarshallingContext();
        Order order = (Order)unmarshallingContext.unmarshalDocument(reader);
        return order;
    }

    public static void main(String[] args)
        throws JiBXException, IOException
    {
        Order order = new Order();
        Address address = new Address();
        address.setCity("hangzhou");
        address.setStreet("xixi road");
        order.setAddress(address);
        Customer customer = new Customer();
        customer.setName("Tony");
        customer.setCustomerNumber(123l);
        order.setCustomer(customer);
        order.setShipping(Shipping.STANDARD_MAIL);
        order.setTotal(123.00f);

        TestOrder testOrder = new TestOrder();
        String str = testOrder.encode2Xml(order);

    }
}
