trait Validator[T] {
  def validate(t: T): Option[ApiError]
}

object CreateAddressValidator extends Validator[CreateAddress] {
  def validate(createAddress: CreateAddress): Option[ApiError] =
    if (createAddress.address.isEmpty)
      Some(ApiError.emptyTitleField)
    else
      None
}

object GetAddressValidator extends Validator[Boolean] {
  def validate(isValid: Boolean): Option[ApiError] =
    if(!isValid)
      Some(ApiError.indexNotFound)
    else
      None
}