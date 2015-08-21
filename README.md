# Test Environment:
Performance tests were completed using the following resources:

CPU: Intel(R) Core(TM) i7-4900MQ CPU @ 2.80GHz
JVM -Xmx=4g
Block Device Type: SSD

For each strategy we executed 2 million searches of a word, sized between 4 and 8 characters in length.

## Disclamers: 
There are a few things I need to point out about how I framed this "analysis":

1.) Compairing the three strategies isn't valid (but I'm going to do it anyway).  Each of these strategies provides different features and non of them are alike (or equal).  Because of this, compairing the performance characteristics of each of these search methods doesn't make sense.

2.) I'm using a lot of memory (-Xmx=4g) and a super fast processor.  It would have been a better test had I limited the resources (1 cpu, 1g ram, iops, etc) so that resource management was a factor.  For that matter - performance tests are generally most reveling when done in a production environment.


# Tests:
## Brute-Force Search (BruteForceSearchStrategy):
* Completed 2 million searches: 119940ms
* min=50506ns max=29113360ns mean=59600ns
* 50=55284ns 75=56572ns 95=75109ns 98=86903ns 99=93279ns
 
## Regex Search (RegexSearchStrategy)
* Completed 2 million searches: 245207ms
* min=105811ns max=37093005ns mean=122151ns
* 50=115200ns 75=121322ns 95=144935ns 98=168984ns 99=193296ns

## HashMap Search (CachedSearchStrategy)
* Completed 2 million searches: 1350ms
* min=84ns max=24168488ns mean=197ns
* 50=135ns 75=193ns 95=258ns 98=422ns 99=480ns

Obviously the in-memory solution provides the best performance.  But scaling this would have it's challenges.

# Scalability
* Need to understand more about the data being searched – this could lead to means for partitioning/clustering data, and load based on context.
* Exploring solutions like Kafka, Cassandra (with Solr) could provide meaningful POCs
