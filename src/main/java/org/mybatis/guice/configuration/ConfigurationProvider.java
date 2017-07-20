/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.configuration;

import com.google.inject.ProvisionException;
import com.google.inject.name.Named;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.configuration.settings.ConfigurationSetting;
import org.mybatis.guice.configuration.settings.ConfigurationSettings;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * Provides the myBatis Configuration.
 */
@Singleton
public class ConfigurationProvider implements Provider<Configuration> {

  /**
   * The myBatis Configuration reference.
   */
  private final Environment environment;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.lazyLoadingEnabled")
  private boolean lazyLoadingEnabled = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.aggressiveLazyLoading")
  private boolean aggressiveLazyLoading = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.multipleResultSetsEnabled")
  private boolean multipleResultSetsEnabled = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.useGeneratedKeys")
  private boolean useGeneratedKeys = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.useColumnLabel")
  private boolean useColumnLabel = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.cacheEnabled")
  private boolean cacheEnabled = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.defaultExecutorType")
  private ExecutorType defaultExecutorType = ExecutorType.SIMPLE;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.autoMappingBehavior")
  private AutoMappingBehavior autoMappingBehavior = AutoMappingBehavior.PARTIAL;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.callSettersOnNulls")
  private boolean callSettersOnNulls = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.defaultStatementTimeout")
  @Nullable
  private Integer defaultStatementTimeout;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.mapUnderscoreToCamelCase")
  private boolean mapUnderscoreToCamelCase = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.failFast")
  private boolean failFast = false;

  @com.google.inject.Inject(optional = true)
  @TypeAliases
  private Map<String, Class<?>> typeAliases = Collections.emptyMap();

  @com.google.inject.Inject(optional = true)
  private Map<Class<?>, TypeHandler<?>> typeHandlers = Collections.emptyMap();

  @com.google.inject.Inject(optional = true)
  @MappingTypeHandlers
  private Set<TypeHandler<?>> mappingTypeHandlers = Collections.emptySet();

  @com.google.inject.Inject(optional = true)
  @Mappers
  private Set<Class<?>> mapperClasses = Collections.emptySet();

  @com.google.inject.Inject(optional = true)
  private Set<Interceptor> plugins = Collections.emptySet();

  @com.google.inject.Inject(optional = true)
  private DatabaseIdProvider databaseIdProvider;

  @com.google.inject.Inject
  private DataSource dataSource;

  @com.google.inject.Inject(optional = true)
  @ConfigurationSettings
  private Set<ConfigurationSetting> configurationSettings = Collections.emptySet();

  /**
   * @since 1.0.1
   */
  @com.google.inject.Inject
  public ConfigurationProvider(final Environment environment) {
    this.environment = environment;
  }

  @Deprecated
  public void setEnvironment(Environment environment) {
    // do nothing
  }

  /**
   * Flag to check all statements are completed.
   *
   * @param failFast
   *          flag to check all statements are completed
   * @since 1.0.1
   */
  public void setFailFast(boolean failFast) {
    this.failFast = failFast;
  }

  /**
   * Adds the user defined type aliases to the myBatis Configuration.
   *
   * @param typeAliases
   *          the user defined type aliases.
   */
  public void setTypeAliases(Map<String, Class<?>> typeAliases) {
    this.typeAliases = typeAliases;
  }

  /**
   * Adds the user defined type handlers to the myBatis Configuration.
   *
   * @param typeHandlers
   *          the user defined type handlers.
   */
  @com.google.inject.Inject(optional = true)
  public void registerTypeHandlers(final Map<Class<?>, TypeHandler<?>> typeHandlers) {
    this.typeHandlers = typeHandlers;
  }

  /**
   * Adds the user defined Mapper classes to the myBatis Configuration.
   *
   * @param mapperClasses
   *          the user defined Mapper classes.
   */
  public void setMapperClasses(Set<Class<?>> mapperClasses) {
    this.mapperClasses = mapperClasses;
  }

  /**
   * Registers the user defined plugins interceptors to the myBatis Configuration.
   *
   * @param plugins
   *          the user defined plugins interceptors.
   */
  public void setPlugins(Set<Interceptor> plugins) {
    this.plugins = plugins;
  }

  public void setConfigurationSettings(Set<ConfigurationSetting> configurationSettings) {
    this.configurationSettings = configurationSettings;
  }

  /**
   * New configuration.
   *
   * @param environment
   *          the environment
   * @return new configuration
   */
  protected Configuration newConfiguration(Environment environment) {
    return new Configuration(environment);
  }

  @Override
  public Configuration get() {
    final Configuration configuration = newConfiguration(environment);
    configuration.setLazyLoadingEnabled(lazyLoadingEnabled);
    configuration.setAggressiveLazyLoading(aggressiveLazyLoading);
    configuration.setMultipleResultSetsEnabled(multipleResultSetsEnabled);
    configuration.setUseGeneratedKeys(useGeneratedKeys);
    configuration.setUseColumnLabel(useColumnLabel);
    configuration.setCacheEnabled(cacheEnabled);
    configuration.setDefaultExecutorType(defaultExecutorType);
    configuration.setAutoMappingBehavior(autoMappingBehavior);
    configuration.setCallSettersOnNulls(callSettersOnNulls);
    configuration.setDefaultStatementTimeout(defaultStatementTimeout);
    configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);

    for (ConfigurationSetting setting : configurationSettings) {
      setting.applyConfigurationSetting(configuration);
    }

    try {
      if (databaseIdProvider != null) {
        configuration.setDatabaseId(databaseIdProvider.getDatabaseId(dataSource));
      }

      for (Map.Entry<String, Class<?>> alias : typeAliases.entrySet()) {
        configuration.getTypeAliasRegistry().registerAlias(alias.getKey(), alias.getValue());
      }

      for (Map.Entry<Class<?>, TypeHandler<?>> typeHandler : typeHandlers.entrySet()) {
        registerTypeHandler(configuration, typeHandler.getKey(), typeHandler.getValue());
      }

      for (TypeHandler<?> typeHandler : mappingTypeHandlers) {
        configuration.getTypeHandlerRegistry().register(typeHandler);
      }

      for (Class<?> mapperClass : mapperClasses) {
        if (!configuration.hasMapper(mapperClass)) {
          configuration.addMapper(mapperClass);
        }
      }

      for (Interceptor interceptor : plugins) {
        configuration.addInterceptor(interceptor);
      }

      if (failFast) {
        configuration.getMappedStatementNames();
      }
    } catch (Throwable cause) {
      throw new ProvisionException(
          "An error occurred while building the org.apache.ibatis.session.Configuration", cause);
    } finally {
      ErrorContext.instance().reset();
    }

    return configuration;
  }

  @SuppressWarnings("unchecked")
  private <T> void registerTypeHandler(Configuration configuration, Class<?> type,
      TypeHandler<?> handler) {
    configuration.getTypeHandlerRegistry().register((Class<T>) type, (TypeHandler<T>) handler);
  }

}
