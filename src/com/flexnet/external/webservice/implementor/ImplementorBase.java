package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.LicenseFileDefinition;
import com.flexnet.external.type.LicenseFileMapItem;
import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.ServiceBase;
import org.apache.commons.lang3.SystemUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class ImplementorBase {

  protected final Log logger = Log.create(this.getClass());

  protected String id() {
    return String.join("-", UUID
            .randomUUID()
            .toString().replace("-","")
            .split("(?<=\\G.{4})"));
  }


  /**
   * 
   */
  class Info {
    class OS {
      public String name;
      public String version;
      public  String architecture;
    }

    class ENV {
      public Integer availableProcessors;
      public Long freeMemory;
      public Long totalMemory;
      public Long maxMemory;
    }

    public OS system;
    public ENV environment;
    public String hostName;
    public String userName;

    @SuppressWarnings("deprecation")
    Info() {
      this.system = new OS() {
        {
          this.name = SystemUtils.OS_NAME;
          this.version = SystemUtils.OS_VERSION;
          this.architecture = SystemUtils.OS_ARCH;
        }
      };

      this.environment = new ENV() {
        {
          final Runtime runtime = Runtime.getRuntime();

          this.availableProcessors = runtime.availableProcessors();
          this.freeMemory = runtime.freeMemory();
          this.totalMemory = runtime.totalMemory();
          this.maxMemory = runtime.maxMemory();
        }
      };

      this.hostName = SystemUtils.getHostName();
      this.userName = SystemUtils.getUserName();
    }
  }

  protected List<LicenseFileMapItem> makeLicenseFiles(final List<LicenseFileDefinition> files, final String text, final byte[] bytes) {
    return new ArrayList<LicenseFileMapItem>() {
      {
        files.forEach(lfd -> {
          switch (lfd.getLicenseStorageType()) {
            case TEXT:
              Optional.ofNullable(text).ifPresent(license -> {
                this.add(new LicenseFileMapItem() {
                  {
                    this.name = lfd.getName();
                    this.value = license;
                  }
                });
              });
              break;
            case BINARY:
              Optional.ofNullable(bytes).ifPresent(license -> {
                this.add(new LicenseFileMapItem() {
                  {
                    this.name = lfd.getName();
                    this.value = license;
                  }
                });
              });
              break;
            default:
              throw new RuntimeException("invalid license file type");
          }
        });
      }
    };
  }

  protected PingResponse ping(final PingRequest request) {
    logger.in();

    return new PingResponse() {
      {
        final Runtime runtime = Runtime.getRuntime();

        this.info = Utils.safeSerializeYaml(new Info());

        this.str = String.format("%s | %s | %s", logger.type().getSimpleName(), ServiceBase.getVersion(), ServiceBase.getBuild());
        this.processedTime = Instant.now().toString();
      }
    };
  }

  public abstract String technologyName();
}
