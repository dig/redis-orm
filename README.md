# redis-orm
A lightweight Object Relational Mapping library for Java. redis-orm is a library for storing objects in Redis, a persistent key-value database.

**Example serializable object:**
```
@AllArgsConstructor
@RedisSerializable
public class Account {
  
  @Id
  private int id;
  
  @Attribute
  private String username;
  
  @Attribute
  private String email;
  
}
```

**Save object:**
```
Account account = new Account(1, "dig", "test@gmail.com");
RedisORM.save(jedis, account);
```

**Get object:**
```
Account account = RedisORM.get(jedis, Account.class, 1);
```
