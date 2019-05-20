package com.company.autotests.product.properties;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import java.net.URI;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"classpath:tests.properties"})
public interface TestProperties extends Config {

    TestProperties PROPERTIES = ConfigFactory.create(TestProperties.class);

    @Key("host")
    URI host();

    @Key("wa_key")
    String waKey();
}
