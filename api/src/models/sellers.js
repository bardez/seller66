export default (sequelize, DataTypes) => {
    const SellersModel = sequelize.define("SellersModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true,
        },
        name: DataTypes.STRING(),
        login: DataTypes.STRING(),
        password: DataTypes.STRING(),
    },{
        tableName: 'sellers',
        timestamps: false,
        paranoid: true,
        createdAt: 'created_at',
        updatedAt: 'updated_at',
        deletedAt: 'deleted_at',
    });
    
    return SellersModel;
}