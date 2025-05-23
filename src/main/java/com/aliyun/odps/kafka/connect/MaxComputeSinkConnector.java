/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package com.aliyun.odps.kafka.connect;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.odps.Odps;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.kafka.connect.utils.OdpsUtils;


public class MaxComputeSinkConnector extends SinkConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(MaxComputeSinkConnector.class);
  private MaxComputeSinkConnectorConfig config;

  public void setConfig(MaxComputeSinkConnectorConfig config) {
    this.config = config;
  }

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  @Override
  public void start(Map<String, String> map) {
    config = new MaxComputeSinkConnectorConfig(map);

    LOGGER.info("Starting MaxCompute sink connector");
    for (Entry<String, String> entry : map.entrySet()) {
      LOGGER.info(entry.getKey() + ": " + entry.getValue());
    }

    Odps odps = OdpsUtils.getOdps(config);

    try {
      odps.projects().exists(config.getString(
          MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_PROJECT.getName()));
      odps.tables().exists(
          config.getString(MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_TABLE.getName()));
    } catch (OdpsException e) {
      throw new IllegalArgumentException("Cannot find configured MaxCompute project or table");
    }

    // TODO: validate table schema

    LOGGER.info("Connect to MaxCompute successfully!");
  }

  @Override
  public Class<? extends Task> taskClass() {
    return MaxComputeSinkTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    LinkedList<Map<String, String>> taskConfigs = new LinkedList<>();
    for (int i = 0; i < maxTasks; i++) {
      Map<String, String> taskConfig = new HashMap<>();
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.ACCESS_ID.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.ACCESS_ID.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.TUNNEL_ENDPOINT.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.TUNNEL_ENDPOINT.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.ACCESS_KEY.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.ACCESS_KEY.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.ACCOUNT_ID.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.ACCOUNT_ID.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.REGION_ID.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.REGION_ID.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.STS_ENDPOINT.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.STS_ENDPOINT.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.ROLE_NAME.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.ROLE_NAME.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.ACCOUNT_TYPE.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.ACCOUNT_TYPE.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.CLIENT_TIMEOUT_MS.getName(),
                     String.valueOf(
                         config.getLong(
                             MaxComputeSinkConnectorConfig.BaseParameter.CLIENT_TIMEOUT_MS.getName())));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_PROJECT.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_PROJECT.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_SCHEMA.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_SCHEMA.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_ENDPOINT.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_ENDPOINT.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_TABLE.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.MAXCOMPUTE_TABLE.getName()));
      taskConfig.put(
          MaxComputeSinkConnectorConfig.BaseParameter.RUNTIME_ERROR_TOPIC_BOOTSTRAP_SERVERS.getName(),
          config
              .getString(
                  MaxComputeSinkConnectorConfig.BaseParameter.RUNTIME_ERROR_TOPIC_BOOTSTRAP_SERVERS.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.RUNTIME_ERROR_TOPIC_NAME.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.RUNTIME_ERROR_TOPIC_NAME.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.FORMAT.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.FORMAT.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.MODE.getName(),
                     config.getString(MaxComputeSinkConnectorConfig.BaseParameter.MODE.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.PARTITION_WINDOW_TYPE.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.PARTITION_WINDOW_TYPE.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.TIME_ZONE.getName(),
                     config.getString(
                         MaxComputeSinkConnectorConfig.BaseParameter.TIME_ZONE.getName()));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.USE_STREAM_TUNNEL.getName(),
                     config.getBoolean(
                         MaxComputeSinkConnectorConfig.BaseParameter.USE_STREAM_TUNNEL.getName())
                     ? "TRUE"
                     : "FALSE");
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.BUFFER_SIZE_KB.getName(),
                     Integer.toString(config.getInt(
                         MaxComputeSinkConnectorConfig.BaseParameter.BUFFER_SIZE_KB.getName())));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.FAIL_RETRY_TIMES.getName(),
                     Integer.toString(
                         config.getInt(
                             MaxComputeSinkConnectorConfig.BaseParameter.FAIL_RETRY_TIMES.getName())));

      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.POOL_SIZE.getName(),
                     Integer.toString(config.getInt(
                         MaxComputeSinkConnectorConfig.BaseParameter.POOL_SIZE.getName())));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.RECORD_BATCH_SIZE.getName(),
                     Integer.toString(
                         config.getInt(
                             MaxComputeSinkConnectorConfig.BaseParameter.RECORD_BATCH_SIZE.getName())));
      taskConfig.put(MaxComputeSinkConnectorConfig.BaseParameter.SKIP_ERROR.getName(),
                     config.getBoolean(
                         MaxComputeSinkConnectorConfig.BaseParameter.SKIP_ERROR.getName()) ? "TRUE"
                                                                                           : "FALSE");
      taskConfigs.push(taskConfig);
    }

    return taskConfigs;
  }

  @Override
  public void stop() {
    // do nothing
  }

  @Override
  public ConfigDef config() {
    return MaxComputeSinkConnectorConfig.conf();
  }
}
