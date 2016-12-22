package messagepack;

import org.msgpack.annotation.Message;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Message
public class Person
{

    private int age;

    private String name;

    private float account;

    public Person()
    {
    }

    public Person(int age, String name, float account)
    {
        this.age = age;
        this.name = name;
        this.account = account;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public float getAccount()
    {
        return account;
    }

    public void setAccount(float account)
    {
        this.account = account;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("age=").append(age);
        sb.append(", name='").append(name).append('\'');
        sb.append(", account=").append(account);
        sb.append('}');
        return sb.toString();
    }
}
