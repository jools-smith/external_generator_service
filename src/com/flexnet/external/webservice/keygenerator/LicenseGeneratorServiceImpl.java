package com.flexnet.external.webservice.keygenerator;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface",
        wsdlLocation = "WEB-INF/wsdl/schema/LicenseGeneratorService.wsdl"
)
public class LicenseGeneratorServiceImpl extends ServiceBase implements LicenseGeneratorServiceInterface {
  /**
   *
   */
  public LicenseGeneratorServiceImpl() {
    super.logger.me(this);
  }


  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public PingResponse ping(final PingRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return super.factory.getDefaultImplementor(LicenseGeneratorServiceInterface.class).ping(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public Status validateProduct(final ProductRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, LicenseGeneratorServiceInterface.class).validateProduct(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public Status validateLicenseModel(final LicenseModelRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, LicenseGeneratorServiceInterface.class).validateLicenseModel(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, LicenseGeneratorServiceInterface.class).generateLicense(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, LicenseGeneratorServiceInterface.class).consolidateFulfillments(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public LicenseFileDefinitionMap generateLicenseFilenames(final GeneratorRequest payload)
      throws LicGeneratorException {
    super.logger.in();
    final Token token = token();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, LicenseGeneratorServiceInterface.class).generateLicenseFilenames(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {

      token.commit();
    }
  }

  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public LicenseFileDefinitionMap generateConsolidatedLicenseFilenames(final ConsolidatedLicenseResquest payload)
      throws LicGeneratorException {
    super.logger.in();
    final Token token = token();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, LicenseGeneratorServiceInterface.class).generateConsolidatedLicenseFilenames(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {

      token.commit();
    }
  }

  /**
   *
   * @param payload
   * @return
   * @throws LicGeneratorException
   */
  @Override
  public String generateCustomHostIdentifier(final HostIdRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();

    try {
      return super.factory.getDefaultImplementor(LicenseGeneratorServiceInterface.class).generateCustomHostIdentifier(payload);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {

      token.commit();
    }
  }
}
