export default (sequelize, DataTypes) => {
    const RouteClientsModel = sequelize.define("RouteClientsModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        route_id: DataTypes.INTEGER(),
        client_id: DataTypes.INTEGER(),
    },{
        tableName: 'route_clients',
        timestamps: false,
        paranoid: true,
        createdAt: 'created_at',
        updatedAt: 'updated_at',
        deletedAt: 'deleted_at',
        
    });
    
    return RouteClientsModel;
}