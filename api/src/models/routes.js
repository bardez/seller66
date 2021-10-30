export default (sequelize, DataTypes) => {
    const RoutesModel = sequelize.define("RoutesModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        name: DataTypes.STRING(50)
    },{
        tableName: 'routes',
        timestamps: false,
        paranoid: true,
        createdAt: 'created_at',
        updatedAt: 'updated_at',
        deletedAt: 'deleted_at',
        
    });
    
    return RoutesModel;
}