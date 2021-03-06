import { InvoiceItemsModel } from '../../models'
import HTTP from '../../utils/http_headers'
import { sequelize } from '../../models'
import {
    resultSuccess,
    resultError,
    resultEmpty
} from '../../utils/response';
const fields = [
    'product_id',
    "invoice_id",
    "quantity"
]
// const getAll = async (req, res) =>{

//   const result = await sequelize.query(`
//           SELECT
//               * 
//           FROM
//             invoice_items it
//           INNER JOIN products p ON
//           p.id = it.product_id
//     `,{
//       type: sequelize.QueryTypes.SELECT
//     });
    

//     if(result.length > 0){
//         resultSuccess('route', res)(result);
//     }else{
//         resultEmpty(res);
//     }
// }

const getById = async (req, res) =>{
    if(req.params.invoice_id > 0){
        const result = await sequelize.query(`
          SELECT
              it.id, it.invoice_id, it.product_id, it.quantity, p.name, p.active, p.value 
          FROM
              invoice_items it
          INNER JOIN products p ON
            p.id = it.product_id
          WHERE
              it.invoice_id = :invoice_id
            group by p.id
        `,{
            replacements:{
              invoice_id: req.params.invoice_id
            },
            type: sequelize.QueryTypes.SELECT
        });
    

      if(result.length > 0){
          resultSuccess('invoice_items', res)(result);
      }else{
          resultEmpty(res);
      }
    }
    else{
        resultEmpty(res);
    }
}

const upsert = async (req, res) =>{
  try {
      const invoiceItemsData = req.body;
      invoiceItemsData.invoice_id = req.params.invoice_id;
      console.log('InvoicesItemsController.upsert',invoiceItemsData);
      const invoiceItems = await InvoiceItemsModel.findOne({ where: { invoice_id: req.params.invoice_id, product_id: invoiceItemsData.product_id } });

      if(invoiceItems) {    
          await invoiceItems.update(invoiceItemsData, { fields });
          resultSuccess('Dados atualizados com sucesso.', res)(invoiceItems);
      } else {
        console.log('InvoicesItemsController.save',invoiceItemsData);
        const invoiceItem = await InvoiceItemsModel.create(invoiceItemsData, { fields });
        resultSuccess('Dados salvos com sucesso.', res)(invoiceItem);
      }
  } catch (error) {
      console.log('InvoicesItemsController.upsert - Erro ao criar registro.', error);
      resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(error);
  }
}

export default{
    // getAll,
    getById,
    upsert
}