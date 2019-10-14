package provider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.json.Json;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for Validation Exceptions
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(final ConstraintViolationException constraintViolationException) {
		final List<String> exceptions = new ArrayList<>();

		constraintViolationException.getConstraintViolations().stream()
				.forEach(constraintViolation -> exceptions.add(buildMessage(constraintViolation)));

		return Response.status(500).entity(exceptions).build();
	}

	String buildMessage(final ConstraintViolation<?> constraintViolation) {
		return Json.createObjectBuilder() //
				.add("message", constraintViolation.getMessage()) //
				.add("violationCode",
						((Annotation) constraintViolation.getConstraintDescriptor().getAnnotation()).annotationType()
								.getSimpleName().toUpperCase()) //
				.add("property", determinePropertyName(constraintViolation.getPropertyPath())) //
				.add("value",
						constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString()
								: null) //
				.build().toString();
	}

	String determinePropertyName(final Path propertyPath) {
		final Optional<Path.Node> propertyName = StreamSupport.stream(propertyPath.spliterator(), false)
				.filter(node -> node.getKind() == ElementKind.PROPERTY).findFirst();

		return propertyName.isPresent() ? propertyName.get().getName() : "";
	}

}