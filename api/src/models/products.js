export default (sequelize, DataTypes) => {
    const ProductsModel = sequelize.define("ProductsModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true,
        },
        name: DataTypes.STRING(),
        active: DataTypes.INTEGER(),
        value: DataTypes.DECIMAL(10,2)
    },{
        tableName: 'products',
        timestamps: false,
        paranoid: true,
        createdAt: 'created_at',
        updatedAt: 'updated_at',
        deletedAt: 'deleted_at',
    });
    
    return ProductsModel;
}