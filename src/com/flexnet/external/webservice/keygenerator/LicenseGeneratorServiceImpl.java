package com.flexnet.external.webservice.keygenerator;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.remote.*;

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
    final Executor<PingRequest, Ping, PingResponse> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.ping, payload)
              .decode(Ping.class)
              .encode(Ping.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
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
    final Executor<ProductRequest, UnknownReply, Status> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.LGI_validateProduct, payload)
              .decode(UnknownReply.class)
              .encode((v) -> new Status())
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
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
    final Executor<LicenseModelRequest, UnknownReply, Status> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.LGI_validateLicenseModel, payload)
              .decode(UnknownReply.class)
              .encode((v) -> new Status())
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
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
    final Executor<GeneratorRequest, License, GeneratorResponse> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.LGI_generateLicense, payload)
              .decode(License.class)
              .encode(License.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
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
    final Executor<FulfillmentRecordSet, UnknownReply, ConsolidatedLicense> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.LGI_consolidateFulfillments, payload)
              .decode(UnknownReply.class)
              .encode((v) -> new ConsolidatedLicense())
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
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
    final Executor<GeneratorRequest, UnknownReply, LicenseFileDefinitionMap> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.LGI_generateLicenseFilenames, payload)
              .decode(UnknownReply.class)
              .encode((v) -> new LicenseFileDefinitionMap())
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
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
    final Executor<ConsolidatedLicenseResquest, UnknownReply, LicenseFileDefinitionMap> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.LGI_generateConsolidatedLicenseFilenames, payload)
              .decode(UnknownReply.class)
              .encode((v) -> new LicenseFileDefinitionMap())
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
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
    final Executor<HostIdRequest, UnknownReply, String> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.LGI_generateCustomHostIdentifier, payload)
              .decode(UnknownReply.class)
              .encode((v) -> "")
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
}
