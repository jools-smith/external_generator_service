package com.flexnet.external.webservice.keygenerator;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.ServiceBase;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface",
        wsdlLocation = "WEB-INF/wsdl/schema/LicenseGeneratorService.wsdl"
)
public class LicenseGeneratorServiceImpl extends ServiceBase implements LicenseGeneratorServiceInterface {

  @Override
  public PingResponse ping(final PingRequest payload) throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();
    try {
      return getImplementorFactory().getDefaultImplementor().ping(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  @Override
  public Status validateProduct(final ProductRequest payload) throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return getImplementorFactory().getImplementor(tech).validateProduct(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest payload) throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return getImplementorFactory().getImplementor(tech).validateLicenseModel(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest payload) throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return getImplementorFactory().getImplementor(tech).generateLicense(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet payload) throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return getImplementorFactory().getImplementor(tech).consolidateFulfillments(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  @Override
  public LicenseFileDefinitionMap generateLicenseFilenames(final GeneratorRequest payload)
      throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return getImplementorFactory().getImplementor(tech).generateLicenseFilenames(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }


  @Override
  public LicenseFileDefinitionMap generateConsolidatedLicenseFilenames(final ConsolidatedLicenseResquest payload)
      throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return getImplementorFactory().getImplementor(tech).generateConsolidatedLicenseFilenames(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  @Override
  public String generateCustomHostIdentifier(final HostIdRequest payload) throws LicGeneratorException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = createDiagnosticsToken();

    try {
      return getImplementorFactory().getDefaultImplementor().generateCustomHostIdentifier(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
}
