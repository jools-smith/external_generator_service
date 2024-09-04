package com.flexnet.external.webservice.remote;

public enum EndpointType {
  health,
  ping,
  //IGI_
  IGI_generateEntitlementID,
  IGI_generateLineItemID,
  IGI_generateWebRegKey,
  IGI_generateMaintenanceItemID,
  IGI_generateFulfillmentID,
  IGI_generateConsolidatedLicenseID,
  IGI_generateConsolidatedItemID,
  //LGI_
  LGI_validateProduct,
  LGI_validateLicenseModel,
  LGI_generateLicense,
  LGI_consolidateFulfillments,
  LGI_generateLicenseFilenames,
  LGI_generateConsolidatedLicenseFilenames,
  LGI_generateCustomHostIdentifier,
  //RenewalServiceInterface
  RSI_request,
  //RenewalRedirectServiceInterface
  RRI_request
}