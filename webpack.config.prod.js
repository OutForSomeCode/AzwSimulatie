const Webpack = require('webpack');
const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'production',
  devtool: 'source-map',
  bail: true,
  output: {
    filename: 'js/[name].[chunkhash:8].js',
    chunkFilename: 'js/[name].[chunkhash:8].chunk.js'
  },
  plugins: [
    new Webpack.DefinePlugin({
      'WEBSOCKETHOST': JSON.stringify('wss://sim.a-random-vps.cf/connectToSimulation'),
      'process.env.NODE_ENV': JSON.stringify('production'),
      'HOST': JSON.stringify('http://sim.a-random-vps.cf/')
    }),
    new Webpack.optimize.ModuleConcatenationPlugin(),
    /*new MiniCssExtractPlugin({
      filename: 'bundle.css'
    })*/
  ]
});
