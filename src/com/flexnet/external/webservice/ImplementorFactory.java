package com.flexnet.external.webservice;

import com.flexnet.external.webservice.idgenerator.IdGeneratorServiceInterface;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;
import com.flexnet.external.webservice.renewal.RenewalServiceInterface;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ImplementorFactory {

  final List<Triple<String, Class<?>, Object>> data = new ArrayList<>();

  public ImplementorFactory() {
    this.data.add(Triple.of("DEF", IdGeneratorServiceInterface.class, new DefaultIdGenerator()));
    this.data.add(Triple.of("DEF", LicenseGeneratorServiceInterface.class, new DefaultLicenseGenerator()));
    this.data.add(Triple.of("DEF", RenewalServiceInterface.class, new DefaultRenewalService()));
  }

  public <T> T getImplementor(final String technology, final Class<T> type) {

    return type.cast(data.stream().filter(x -> x.getLeft().equals(technology) && x.getMiddle() == type).findFirst().get().getRight());
  }
}
