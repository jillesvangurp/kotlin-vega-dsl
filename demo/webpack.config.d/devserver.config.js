if (config.devServer) {
    // all options:
    //object { allowedHosts?, bonjour?, client?, compress?, devMiddleware?, headers?,
    // historyApiFallback?, host?, hot?, http2?, https?, ipc?, liveReload?, magicHtml?,
    // onAfterSetupMiddleware?, onBeforeSetupMiddleware?, onListening?, open?, port?, proxy?,
    // server?, setupExitSignals?, setupMiddlewares?, static?, watchFiles?, webSocketServer? }

    // just reload manually when the compiler is actually done and avoid waiting twice for the app
    // to initialize
    config.devServer.open = false
    config.devServer.hot = false
    config.devServer.liveReload = false

    // enable/disable fullscreen error overlay
    config.devServer.client.overlay = false
}
