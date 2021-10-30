export default (sequelize, DataTypes) => {

    const InvoiceItemsModel = sequelize.define("InvoiceItemsModel", {
        id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        product_id: {
            type: DataTypes.INTEGER(),
            references:{
                model: 'Product',
                key: 'id'
            }
        },
        invoice_id: {
            type: DataTypes.INTEGER(),
            references:{
                model: 'Invoice',
                key: 'id'
            }
        },
        quantity: DataTypes.DECIMAL(10,2)
    },{
        tableName: 'invoice_items',
        timestamps: true,
        paranoid: true,
        createdAt: 'created_at',
        updatedAt: 'updated_at',
        deletedAt: 'deleted_at',
    });


    return InvoiceItemsModel;
}