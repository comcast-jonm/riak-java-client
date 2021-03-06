package com.basho.riak.client.api.commands.indexes;

import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.indexes.IndexNames;

/**
 * Performs a 2i query across the special $bucket index, for a known bucket, and returns the keys in that bucket.
 * <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
 * <p>
 * A BucketIndexQuery is used when you want to fetch all the keys for a bucket. Only a namespace is needed.
 * </p>
 * <pre class="prettyprint">
 * {@code
 * Namespace ns = new Namespace("my_type", "my_bucket");
 * BucketIndexQuery q = new BucketIndexQuery.Builder(ns).build();
 * RawIndexquery.Response resp = client.execute(q);}</pre>
 *
 * <p>
 * You can also stream the results back before the operation is fully complete.
 * This reduces the time between executing the operation and seeing a result,
 * and reduces overall memory usage if the iterator is consumed quickly enough.
 * The result iterable can only be iterated once though.
 * If the thread is interrupted while the iterator is polling for more results,
 * a {@link RuntimeException} will be thrown.
 * <pre class="prettyprint">
 * {@code
 * Namespace ns = new Namespace("my_type", "my_bucket");
 * BucketIndexQuery q = new BucketIndexQuery.Builder(ns).build();
 * RiakFuture<BinIndexQuery.StreamingResponse, BinIndexQuery> streamingFuture =
 *     client.executeAsyncStreaming(q, 200);
 * BinIndexQuery.StreamingResponse streamingResponse = streamingFuture.get();
 *
 * for (BinIndexQuery.Response.Entry e : streamingResponse)
 * {
 *     System.out.println(e.getRiakObjectLocation().getKey().toString());
 * }
 * // Wait for the command to fully finish.
 * streamingFuture.await();
 * // The StreamingResponse will also contain the continuation, if the operation returned one.
 * streamingResponse.getContinuation(); }</pre>
 * </p>
 *
 * @author Alex Moore <amoore at basho dot com>
 * @since 2.0.7
 */
public class BucketIndexQuery extends BinIndexQuery
{
    private BucketIndexQuery(Init<String, Builder> builder)
    {
        super(builder);
    }

    public static class Builder extends BinIndexQuery.Init<String, Builder>
    {
        public Builder(Namespace namespace)
        {
            super(namespace, IndexNames.BUCKET, namespace.getBucketName().toStringUtf8());
        }

        public Builder(Namespace namespace, byte[] coverContext)
        {
            super(namespace, IndexNames.BUCKET, coverContext);
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        public BucketIndexQuery build()
        {
            return new BucketIndexQuery(this);
        }
    }
}
