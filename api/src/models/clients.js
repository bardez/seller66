export default (sequelize, DataTypes) => {
    const ClientsModel = sequelize.define("ClientsModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true,
        },
        name: DataTypes.STRING(),
        phone: DataTypes.STRING(),
        address: DataTypes.STRING(),
        state: DataTypes.STRING(),
    },{
        tableName: 'clients',
        timestamps: false,
        paranoid: true,
        createdAt: 'created_at',
        updatedAt: 'updated_at',
        deletedAt: 'deleted_at',
    });
    
    return ClientsModel;
}