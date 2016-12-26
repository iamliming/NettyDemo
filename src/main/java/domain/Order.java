package domain;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 24, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Order
{
    private long number;

    private Customer customer;

    private Address address;

    private Shipping shipping;

    private Float total;

    public long getNumber()
    {
        return number;
    }

    public void setNumber(long number)
    {
        this.number = number;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public Shipping getShipping()
    {
        return shipping;
    }

    public void setShipping(Shipping shipping)
    {
        this.shipping = shipping;
    }

    public Float getTotal()
    {
        return total;
    }

    public void setTotal(Float total)
    {
        this.total = total;
    }
}
