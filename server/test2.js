const { BetaAnalyticsDataClient } = require('@google-analytics/data')

const client = new BetaAnalyticsDataClient({
  keyFilename: "./analtyicsKey.json"
})


async function runSample() {
  const propertyId = '514093218'; 
  try {
    const [response] = await client.runReport({
      property: `properties/${propertyId}`,
      dimensions: [{ name: 'city' }, { name: 'country' }], // نمونه
      metrics: [{ name: 'activeUsers' }, { name: 'newUsers' }],
      dateRanges: [{ startDate: '2025-01-01', endDate: 'today' }],
      limit: 100
    });

    console.log('Report rows:');
    if (!response.rows) {
      console.log('هیچ داده‌ای بازنگشت.');
      return;
    }
    for (const row of response.rows) {
      const dims = row.dimensionValues.map(d => d.value).join(' | ');
      const mets = row.metricValues.map(m => m.value).join(' | ');
      console.log(dims + '  =>  ' + mets);
    }
  } catch (err) {
    console.error('Error running report:', err);
  }
}

runSample();