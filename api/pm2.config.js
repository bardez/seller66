module.exports = {
    apps : [{
      name      : 'api_seller66',
      script    : 'app.js',
      node_args : '-r dotenv-flow/config',
      env:{
        NODE_ENV: ''
      }
    }]
  }