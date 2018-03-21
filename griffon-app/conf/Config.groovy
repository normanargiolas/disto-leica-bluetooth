application {
    title = 'disto-leica'
    startupGroups = ['distoLeica']
    autoShutdown = true

    initialSize {
        width = 650
        height = 300
    }
}
mvcGroups {
    // MVC Group for "distoLeica"
    'distoLeica' {
        model      = 'nor.example.leica.DistoLeicaModel'
        view       = 'nor.example.leica.DistoLeicaView'
        controller = 'nor.example.leica.DistoLeicaController'
    }
}