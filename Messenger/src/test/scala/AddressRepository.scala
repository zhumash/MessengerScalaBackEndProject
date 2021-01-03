import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}

trait AddressRepository {
  def all(): Future[Seq[Address]]
  def validIndex(idx : Long) : Boolean
  def create(createTodo:CreateAddress): Future[Address]
  def get(idx : Long) : Address
  def delete(idx : Long) : Unit
  def update(idx : Long, address: CreateAddress) : Future[Address]
}


class InMemoryAddressRepository(todo:Seq[Address] = Seq.empty)(implicit  ex:ExecutionContext) extends AddressRepository {
  private var addresses: Vector[Address] = todo.toVector
  override def update(idx : Long, address: CreateAddress) : Future[Address] = Future.successful{

    val newAddress : Address = Address(UUID.randomUUID().toString, address.name, address.secondName, address.address, address.phoneNumber)
    addresses = addresses.updated(idx.toInt - 1, newAddress)
    newAddress
  }
  override def delete(idx: Long): Unit = {
    addresses = addresses.slice(0, idx.toInt - 1) ++ addresses.drop(idx.toInt)
  }
  override def all(): Future[Seq[Address]] = Future.successful(addresses)

  override def get(idx: Long): Address = {
    addresses(idx.toInt - 1)
  }
  override def validIndex(idx : Long): Boolean = {
    return idx > 0 && idx <= addresses.length
  }
  override def create(createAddres: CreateAddress): Future[Address] = Future.successful {
    val todo = Address(
      id = UUID.randomUUID().toString,
      name = createAddres.name,
      secondName = createAddres.secondName,
      address = createAddres.address,
      phoneNumber = createAddres.phoneNumber
    )
    addresses = addresses :+ todo
    todo
  }
}
case class Address(id: String, name: String, secondName:String, address : String, phoneNumber : String)
case class CreateAddress(name: String, secondName:String, address : String, phoneNumber : String)