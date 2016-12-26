package domain;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 24, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Customer
{
    private String name;

    private long customerNumber;

    public long getCustomerNumber()
    {
        return customerNumber;
    }

    public void setCustomerNumber(long customerNumber)
    {
        this.customerNumber = customerNumber;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
