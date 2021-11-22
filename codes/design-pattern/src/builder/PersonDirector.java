package builder;

public class PersonDirector {
    public Person constructPerson(ConcreteBuilder builder) {
        builder.buildHead();
        builder.buildBody();
        builder.buildFoot();

        return builder.buildPerson();
    }
}
