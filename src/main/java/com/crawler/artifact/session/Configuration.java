package com.crawler.artifact.session;

import java.util.*;

import com.crawler.artifact.binding.MapperRegistry;
import com.crawler.artifact.binding.ScheduleRegistry;
import com.crawler.artifact.http.httpclient.HttpCrawler;
import com.crawler.artifact.mapping.MappedStatement;
import com.crawler.artifact.type.CrawlerType;
import com.crawler.artifact.util.CacheMap;
import com.crawler.artifact.binding.ScheduleExecutor;
import com.crawler.artifact.logging.LogFactory;
import com.crawler.artifact.mapping.GlobalHeaders;
import com.crawler.artifact.mapping.MappedProxyType;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 几乎全部配置信息,配置到Configuration实例中
 */

public class Configuration {

    protected Properties variables = new Properties();
    protected final Set<String> loadedResources = new HashSet<String>();
    private boolean cat = false;
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);
    protected ScheduleRegistry scheduleRegistry = new ScheduleRegistry(this);
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<String, MappedStatement>();
    protected final Map<String, GlobalHeaders> mappedGlobalHeaders = new StrictMap<GlobalHeaders>("Mapped GlobalHeaders collection");
    protected final Map<String, String> mappedGlobalCharset = new StrictMap<String>("Mapped GlobalCharset collection");
    protected final Map<String, CrawlerType> mappedGlobalCrawlerType = new StrictMap<CrawlerType>("Mapped GlobalCrawlerType collection");
    protected final Map<String, MappedProxyType> mappedGlobalProxy = new StrictMap<MappedProxyType>("Mapped GlobalProxy collection");
    protected final Map<String, Map<String, HttpCrawler>> mappedMapperCacheTimeout = new HashMap<String, Map<String, HttpCrawler>>();
    protected long globalCacheTimeout = 5 * 60;

    public void addMapperCacheByTimeout(String id, long timeout) {
        LogFactory.logInfo("configuration addMapperCacheByTimeout id=" + id + ",timeout=" +
                timeout + ".");
        mappedMapperCacheTimeout.put(id, new CacheMap<String, HttpCrawler>(timeout));
    }

    public Map<String, HttpCrawler> getMapperCache(String id) {
        Map<String, HttpCrawler> cache = mappedMapperCacheTimeout.get(id);
        if (cache == null) {
            Map<String, HttpCrawler> cacheMap = new CacheMap<>(globalCacheTimeout);
            mappedMapperCacheTimeout.put(id, cacheMap);
            return cacheMap;
        }
        return cache;
    }

    public void setCat(boolean cat) {
        this.cat = cat;
    }

    public boolean isCat() {
        return cat;
    }

    public Map<String, CrawlerType> getMappedGlobalCrawlerType() {
        return mappedGlobalCrawlerType;
    }

    public Map<String, Map<String, HttpCrawler>> getMappedMapperCacheTimeout() {
        return mappedMapperCacheTimeout;
    }

    public Set<String> getLoadedResources() {
        return loadedResources;
    }

    public Map<String, MappedStatement> getMappedStatements() {
        return mappedStatements;
    }

    public Map<String, GlobalHeaders> getMappedGlobalHeaders() {
        return mappedGlobalHeaders;
    }

    public Map<String, String> getMappedGlobalCharset() {
        return mappedGlobalCharset;
    }

    public Map<String, MappedProxyType> getMappedGlobalProxy() {
        return mappedGlobalProxy;
    }

    public void setMapperRegistry(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }

    public ScheduleRegistry getScheduleRegistry() {
        return scheduleRegistry;
    }

    public void setScheduleRegistry(ScheduleRegistry scheduleRegistry) {
        this.scheduleRegistry = scheduleRegistry;
    }

    public long getGlobalCacheTimeout() {
        return globalCacheTimeout;
    }

    public void setGlobalCacheTimeout(long globalCacheTimeout) {
        this.globalCacheTimeout = globalCacheTimeout;
    }

    public Properties getVariables() {
        return variables;
    }
    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }
    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public void addMappedStatement(MappedStatement ms) {
        LogFactory.logInfo("configuration addMappedStatement id=" + ms.getId() + ",mappedStatement=" +
                ms + ".");
        mappedStatements.put(ms.getId(), ms);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public boolean hasSchedule(String id) {
        return  scheduleRegistry.hasSchedule(id);
    }

    public ScheduleExecutor getSchedule(String id){
        return scheduleRegistry.getSchedule(id);
    }

    public void addSchedule(String id, String location, String threadnum, String timeout){
        scheduleRegistry.addSchedule(id, location, threadnum, timeout);
    }

    public <T> T getMapper(Class<T> type, CrawlerSession crawlerSession) {
        return mapperRegistry.getMapper(type, crawlerSession);
    }

    public MappedStatement getMappedStatement(String id) {
        MappedStatement mappedStatement = mappedStatements.get(id);
        if(mappedStatement != null){
            return mappedStatement;
        }
        return new MappedStatement();
    }

    public void addGlobalHeaders(GlobalHeaders globalHeaders) {
        LogFactory.logInfo("configuration addGlobalHeaders id=" + globalHeaders.getId() + ",globalHeaders=" +
                globalHeaders + ".");
        mappedGlobalHeaders.put(globalHeaders.getId(), globalHeaders);
    }

    public GlobalHeaders getGlobalHeaders(String id) {
        return mappedGlobalHeaders.get(id);
    }

    public void addGlobalCharset(String id, String charset) {
        LogFactory.logInfo("configuration addGlobalCharset id=" + id + ",charset=" +
                charset + ".");
        mappedGlobalCharset.put(id, charset);
    }

    public void addGlobalCrawlerType(String id, CrawlerType crawlerType) {
        LogFactory.logInfo("configuration addGlobalCrawlerType id=" + id + ",crawlerType=" +
                crawlerType + ".");
        mappedGlobalCrawlerType.put(id, crawlerType);
    }

    public String getGlobalCharset(String id) {
        if(mappedGlobalCharset.containsKey(id)){
            return mappedGlobalCharset.get(id);
        }
        return null;
    }

    public CrawlerType getGlobalCrawlerType(String id) {
        if(mappedGlobalCrawlerType.containsKey(id)){
            return mappedGlobalCrawlerType.get(id);
        }
        return null;
    }

    public void addGlobalProxyType(MappedProxyType mappedProxyType) {
        LogFactory.logInfo("configuration addGlobalProxyType id=" + mappedProxyType.getId() + ",mappedProxyType=" +
                mappedProxyType + ".");
        mappedGlobalProxy.put(mappedProxyType.getId(), mappedProxyType);
    }

    public MappedProxyType getGlobalProxyType(String id) {
        if(mappedGlobalProxy.containsKey(id)){
            return mappedGlobalProxy.get(id);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "variables=" + variables +
                ", loadedResources=" + loadedResources +
                ", mappedStatements=" + mappedStatements +
                '}';
    }

    //静态内部类,严格的Map，不允许多次覆盖key所对应的value
    protected static class StrictMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -4950446264854982944L;
        private String name;

        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.name = name;
        }

        public StrictMap(String name, int initialCapacity) {
            super(initialCapacity);
            this.name = name;
        }

        public StrictMap(String name) {
            super();
            this.name = name;
        }

        public StrictMap(String name, Map<String, ? extends V> m) {
            super(m);
            this.name = name;
        }

        @SuppressWarnings("unchecked")
        public V put(String key, V value) {
            if (containsKey(key)) {
                //如果已经存在此key了，直接报错
                throw new IllegalArgumentException(name + " already contains value for " + key);
            }
            return super.put(key, value);
        }

        public V get(Object key) {
            V value = super.get(key);
            //如果找不到相应的key，直接报错
            if (value == null) {
                throw new IllegalArgumentException(name + " does not contain value for " + key);
            }
            //如果是模糊型的，也报错，提示用户
            //原来这个模糊型就是为了提示用户啊
            if (value instanceof Ambiguity) {
                throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
                        + " (try using the full name including the namespace, or rename one of the entries)");
            }
            return value;
        }

        //取得短名称，也就是取得最后那个句号的后面那部分
        private String getShortName(String key) {
            final String[] keyparts = key.split("\\.");
            return keyparts[keyparts.length - 1];
        }

        //模糊，居然放在Map里面的一个静态内部类，
        protected static class Ambiguity {
            //提供一个主题
            private String subject;

            public Ambiguity(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return subject;
            }
        }
    }

}
