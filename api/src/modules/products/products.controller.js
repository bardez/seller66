import {
    resultSuccess,
    resultError,
    resultEmpty
} from '../../utils/response';
import {
    ProductsModel,
    ProductMediaModel,
    CustomFieldsProductModel,
    BidsModel,
    TypesModel,
    sequelize,
    Sequelize
} from '../../models'
import HTTP from '../../utils/http_headers'
import { saveFile, deleteFile } from '../../utils/helper';
import { paginatedResults } from '../../utils/paginatedResults'

const productFilePath = `${process.env.ASSETS_PATH}/products`
const fields = [
    'name',
    'status_id',
    'type_id',
]

const customFields = [
    'product_id',
    'custom_fields_id',
    'value'
]

const getAll = async (req, res) =>{

    const excludedFields = [
        'created_at',
        'updated_at',
    ];
    // const inclusions = [
    //     { model: TypesModel, as: 'type' }
    // ];

    // const searchFields = [ '`type`.`name`', '`ProductsModel`.`name`'];

    const data = await paginatedResults(ProductsModel, req.query, excludedFields, [], []);

    const returnData = {
        count: data.count,
        rows: data.rows
    }

    if(returnData.rows.length > 0){
        resultSuccess('Listagem de produtos', res)(returnData);
    }else{
        resultEmpty(res);
    }
}

const getAllByType = async (req, res) =>{

    const excludedFields = [
        'created_at',
        'updated_at',
    ];

    req.query.extraFields = [
        ['type_id', req.params.id, Sequelize.Op.eq]
    ]

    const searchFields = [ 'name'];
    const data = await paginatedResults(ProductsModel, req.query, excludedFields, searchFields);

    const returnData = {
        count: data.count,
        rows: data.rows
    }

    if(returnData.rows.length > 0){
        resultSuccess('Listagem de produtos', res)(returnData);
    }else{
        resultEmpty(res);
    }
}

const getById = async (req, res) =>{
    if(req.params.id > 0){
        const mediaModel = {
            model: ProductMediaModel,
            as: 'product_media'
        };

        const data = await ProductsModel.findOne({
            where: {
                id: req.params.id
            },
            include: [
                mediaModel,
                { model: CustomFieldsProductModel, as: 'product_custom_fields' }
            ],
            order:[
                [
                    mediaModel,
                    'position',
                    'ASC'
                ]
            ]
        });
        if(data == null){
            resultEmpty(res);
        }else{
            resultSuccess('Listagem', res)(data);
        }
    }
}

const saveProductImageFile = async (images, product_id) =>{
    let result = [];
    if(images && images.length > 0){
        for(const obj of images){
            let filePath = `${productFilePath}/${product_id}`;
            result.push({
                product_id,
                name: await saveFile({ file_path: filePath, file: obj.name}),
                featured: obj.featured,
                position: obj.position,
                type: 'I'
            });
        }
    }
    return result;
}

const saveMedia = async (data, product_id, transaction) =>{
    const filteredImages = data.filter(item=>item.type == 'I');
    if(filteredImages.length > 0){
        const imagesToSave = await saveProductImageFile(filteredImages, product_id);
        await ProductMediaModel.bulkCreate(imagesToSave, { returning: true, transaction })
    }

    const filteredVideos = data.filter(item=>(item.type == 'V' || item.type == 'Y'));
    if(filteredVideos.length > 0){
        for( const video of filteredVideos){
            const {
                name,
                featured,
                position
            } = video;
            await ProductMediaModel.create({
                product_id,
                name,
                type: 'V',
                featured,
                position
            }, { transaction })
        }
    }
}

const save = async (req, res) =>{
    const transaction = await sequelize.transaction();
    try {
        let promiseArr = [];
        let { data:productData } = req.body;
        let customFieldsData = Object.assign({}, productData.custom);
        delete productData.custom;

        const product = await ProductsModel.create(productData, { fields, transaction });

        for (const key in customFieldsData) {
            let request = {
                value: customFieldsData[key], 
                custom_fields_id: key, 
                product_id: product.id
            };
            promiseArr.push(await CustomFieldsProductModel.create(request, { customFields, transaction }));
        }
       
        try {
            promiseArr.push(await saveMedia(productData.media, product.id, transaction));
        } catch (error) {
            throw new Error(error);
        }

        Promise.all(promiseArr).then(async response => {
            console.log(response);
            await transaction.commit();
            resultSuccess('Dados salvos com sucesso.', res)(product);
        }).catch( async e => {
            console.log(e);
            await transaction.rollback();
            resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao criar registro.', res)(error);
        });
    } catch (error) {
        await transaction.rollback();
        console.log('ProductController.save - Erro ao criar registro.', error);
        resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao criar registro.', res)(error);
    }
}

const update = async (req, res) => {
    const transaction = await sequelize.transaction();
    try {
        const product = await ProductsModel.findOne({ where: { id: req.params.id } });

        if(product){
            let promiseArr = [];
            let { data:productData } = req.body;
            let customFieldsData = Object.assign({}, productData.custom);
            delete productData.custom;

            await product.update(productData, { fields });

            for (const key in customFieldsData) {
                const customFieldProduct = await CustomFieldsProductModel.findOne({
                    where: { custom_fields_id: key, product_id: product.id }
                });
                const request = {
                    value: customFieldsData[key], 
                    custom_fields_id: key, 
                    product_id: product.id
                };
                if( customFieldProduct ) {
                    promiseArr.push(await customFieldProduct.update(request, { customFields, transaction }));
                } else {
                    promiseArr.push(await CustomFieldsProductModel.create(request, { customFields, transaction }));
                }
            }

            if(productData.media && productData.media.length > 0){
                try {
                    promiseArr.push(await saveMedia(productData.media, product.id, transaction));
                } catch (error) {
                    throw new Error(error);
                }
            }

            if(productData.media_position && productData.media_position.length > 0){
                for( const obj of productData.media_position){
                    const media = await ProductMediaModel.findByPk(obj.id);
                    if(media){
                        await media.update({
                            position: obj.position, featured: obj.featured
                        }, { transaction });
                    }
                }
            }

            Promise.all(promiseArr).then(async response => {
                console.log(response);
                await transaction.commit();
                resultSuccess('Dados atualizados com sucesso.', res)(product);
            }).catch( async e => {
                console.log(e);
                await transaction.rollback();
                console.log('ProductController.update - Erro ao atualizar registro.', e);
                resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(e);
            });
        }
        
    } catch (error) {
        await transaction.rollback();
        console.log('ProductController.update - Erro ao atualizar registro.', error);
        resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(error);
    }
}

const deleteMedia = async (req, res) =>{
    const transaction = await sequelize.transaction();
    try {
        const media = await ProductMediaModel.findByPk(req.params.id);
        await media.destroy({
            where:{
                id: media.id
            }
        }, { transaction });

        if(media && media.type == 'I'){
            await deleteFile(media.name, productFilePath); // delete file
            await deleteFile(media.name, `${productFilePath}/thumb`); // delete thumb
        }
        await transaction.commit();

        resultSuccess('Registro deletado com sucesso.', res)();
    } catch (error) {
        await transaction.rollback();
        console.log('ProductsController.remove - Erro ao deletar registro.', error);
        resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao deletar registro.', res)(error);
    }
}

const bid = async (req, res) =>{

    try {
        let dataModelBid = {
            user_id: 0,
            auction_id: 0,
            lot_id: 0,
            current_value: 0,
            bid_value: 0,
        }

        dataModelBid = req.body.data;

        dataModelBid.user_id = req.user.id;

        // check if is the first bid
        const currentBid = await BidsModel.findOne({
            where:{
                auction_id: dataModelBid.auction_id,
            },
            order: [['id', 'DESC']],
            group: ['id'],
            limit: 1
        });
    
        if(currentBid){
            // increment current bid value with bid value sended by user
            dataModelBid.current_value = (parseFloat(dataModelBid.bid_value) + parseFloat(currentBid.current_value));
        }else{

        }
    
        // saves user bid
        const bidResult = await BidsModel.create(dataModelBid, { fields: ['user_id', 'auction_id', 'lot_id', 'current_value', 'bid_value']})
    
        req.app.get("socketService").emiter('newBid', bidResult);

        return resultSuccess('Lance realizado com sucesso.', res)();
    } catch (error) {
        console.log(error);
    }
}

export default{
    getAll,
    getAllByType,
    getById,
    save,
    update,
    deleteMedia,
    bid,
}