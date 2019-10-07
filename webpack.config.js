const path = require('path');
const webpack = require("webpack");
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    module: {
        rules: [
            {
                test: /\.m?js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            {
                test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: path.resolve(__dirname, 'img/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: path.resolve(__dirname, 'media/[name].[hash:7].[ext]')
                }
            },
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
                loader: 'url-loader',
                options: {
                    limit: 10000,
                    name: path.resolve(__dirname, 'fonts/[name].[hash:7].[ext]')
                }
            },
            {test: /\.(glsl|frag|vert)$/, loader: 'raw-loader', exclude: /node_modules/},
            {test: /\.(glsl|frag|vert)$/, loader: 'glslify-loader', exclude: /node_modules/},
            {test: /node_modules/, loader: 'ify-loader'},
            {test: /\.worker\.js$/, loader: 'worker-loader'}
        ]
    },
    plugins: [
      new webpack.HotModuleReplacementPlugin(),
      // https://github.com/ampedandwired/html-webpack-plugin
      new HtmlWebpackPlugin({
        filename: 'index.html',
        template: 'index.html',
        inject: true
      }),
    ],
    entry: './javascript/SimulationView.js',
    output: {
        filename: 'SimulationView.js',
        // src\main\resources\static
        path: path.resolve(__dirname, 'main', 'resources', 'static')
    }
};
